package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsFC;
import com.ysh.dlt2811bean.scl.SclDocument;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclBDA;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDA;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDAType;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDO;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDOType;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclLNodeType;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclSDO;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.scl.model.SclIED.SclLN;
import com.ysh.dlt2811bean.scl.model.SclIED.SclLN0;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataDefinition;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataDefinitionEntry;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.ArrayList;
import java.util.List;

public class GetAllDataDefinitionHandler extends AbstractCmsServiceHandler<CmsGetAllDataDefinition> {

    private final SclDocument sclDocument;

    public GetAllDataDefinitionHandler(SclDocument sclDocument) {
        super(ServiceName.GET_ALL_DATA_DEFINITION, CmsGetAllDataDefinition::new);
        this.sclDocument = sclDocument;
    }

    @Override
    protected CmsApdu doServerHandle() {

        String ldName = null;
        String lnRef = null;
        boolean useLdName = asdu.reference.getSelectedIndex() == 0;
        if (useLdName) {
            ldName = asdu.reference.ldName.get();
        } else {
            lnRef = asdu.reference.lnReference.get();
        }

        String fcFilter = asdu.fc != null ? asdu.fc.get() : null;
        if (fcFilter != null && (fcFilter.isEmpty() || "XX".equals(fcFilter))) {
            fcFilter = null;
        }

        log.debug("[Server] GetAllDataDefinition: lnRef={}, ldName={}, fc={}", lnRef, ldName, fcFilter);

        List<TargetLn> targets = resolveTargets(ldName, lnRef);
        if (targets == null) {
            log.warn("[Server] resolveTargets returned null for ldName={} lnRef={}", ldName, lnRef);
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        List<DoEntry> entries = collectDoDefinitions(targets, !useLdName, fcFilter);
        log.debug("[Server] collectDoDefinitions: {} entries for {} targets", entries.size(), targets.size());

        String after = asdu.referenceAfter != null ? asdu.referenceAfter.get() : null;
        int startIndex = 0;
        if (after != null && !after.isEmpty()) {
            boolean found = false;
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).ref.equals(after)) {
                    startIndex = i + 1;
                    found = true;
                    break;
                }
            }
            if (!found) {
                log.warn("[Server] referenceAfter not found: {}", after);
                return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
        }

        CmsArray<CmsDataDefinitionEntry> data = new CmsArray<>(CmsDataDefinitionEntry::new)
                .capacity(Math.max(1, entries.size() - startIndex));
        for (int i = startIndex; i < entries.size(); i++) {
            DoEntry de = entries.get(i);
            CmsDataDefinitionEntry entry = new CmsDataDefinitionEntry()
                    .reference(de.ref)
                    .cdcType(de.cdcType)
                    .definition(de.definition);
            data.add(entry);
        }

        CmsGetAllDataDefinition response = new CmsGetAllDataDefinition(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .data(data);
        response.moreFollows.set(false);

        log.debug("[Server] GetAllDataDefinition: {} entries{}", data.size(),
                fcFilter != null ? " (fc=" + fcFilter + ")" : "");
        return new CmsApdu(response);
    }

    private List<DoEntry> collectDoDefinitions(List<TargetLn> targets, boolean relative, String fcFilter) {
        SclDataTypeTemplates templates = sclDocument != null ? sclDocument.getDataTypeTemplates() : null;
        List<DoEntry> result = new ArrayList<>();

        for (TargetLn t : targets) {
            String prefix = relative ? "" : (t.fullName() + ".");
            List<SclDO> dos = getDosForType(templates, t.lnType);
            if (dos == null || dos.isEmpty()) {
                continue;
            }

            for (SclDO doDef : dos) {
                if (templates == null || doDef.getType() == null) continue;
                SclDOType doType = templates.findDoTypeById(doDef.getType());
                if (doType == null) continue;

                List<CmsDataDefinition.StructureEntry> daEntries = buildDaEntries(templates, doType, fcFilter);
                if (daEntries.isEmpty()) {
                    continue;
                }

                String ref = prefix + doDef.getName();
                CmsDataDefinition def = CmsDataDefinition.ofStructure(daEntries);
                String cdc = doType.getCdc();
                result.add(new DoEntry(ref, cdc, def));

                collectSdoEntries(templates, doType, prefix + doDef.getName() + ".", fcFilter, result);
            }
        }
        return result;
    }

    private void collectSdoEntries(SclDataTypeTemplates templates, SclDOType parentDoType,
                                    String parentPrefix, String fcFilter, List<DoEntry> result) {
        if (templates == null) return;
        for (SclSDO sdo : parentDoType.getSdos()) {
            if (sdo.getType() == null) continue;
            SclDOType sdoDoType = templates.findDoTypeById(sdo.getType());
            if (sdoDoType == null) continue;

            List<CmsDataDefinition.StructureEntry> daEntries = buildDaEntries(templates, sdoDoType, fcFilter);
            if (daEntries.isEmpty()) {
                continue;
            }

            String ref = parentPrefix + sdo.getName();
            CmsDataDefinition def = CmsDataDefinition.ofStructure(daEntries);
            String cdc = sdoDoType.getCdc();
            result.add(new DoEntry(ref, cdc, def));
        }
    }

    private List<CmsDataDefinition.StructureEntry> buildDaEntries(SclDataTypeTemplates templates,
                                                                   SclDOType doType, String fcFilter) {
        List<CmsDataDefinition.StructureEntry> entries = new ArrayList<>();
        for (SclDA da : doType.getDas()) {
            if (fcFilter != null && !fcFilter.equals(da.getFc())) {
                continue;
            }
            if (!CmsFC.isValid(da.getFc())) {
                continue;
            }
            CmsDataDefinition daDef = resolveDaType(templates, da);
            if (daDef != null) {
                entries.add(new CmsDataDefinition.StructureEntry(da.getName(), da.getFc(), daDef));
            }
        }
        return entries;
    }

    private CmsDataDefinition resolveDaType(SclDataTypeTemplates templates, SclDA da) {
        return resolveBType(templates, da.getBType(), da.getType(), da.getCount());
    }

    private CmsDataDefinition resolveBType(SclDataTypeTemplates templates, String bType,
                                            String typeRef, Integer count) {
        if (bType == null) return CmsDataDefinition.ofInt32();
        switch (bType) {
            case "BOOLEAN":  return CmsDataDefinition.ofBoolean();
            case "INT8":     return CmsDataDefinition.ofInt8();
            case "INT16":    return CmsDataDefinition.ofInt16();
            case "INT32":    return CmsDataDefinition.ofInt32();
            case "INT64":    return CmsDataDefinition.ofInt64();
            case "INT8U":    return CmsDataDefinition.ofInt8U();
            case "INT16U":   return CmsDataDefinition.ofInt16U();
            case "INT32U":   return CmsDataDefinition.ofInt32U();
            case "INT64U":   return CmsDataDefinition.ofInt64U();
            case "FLOAT32":  return CmsDataDefinition.ofFloat32();
            case "FLOAT64":  return CmsDataDefinition.ofFloat64();
            case "BIT STRING":
                return CmsDataDefinition.ofBitString(count != null ? count : 0);
            case "OCTET STRING":
                return CmsDataDefinition.ofOctetString(count != null ? count : 255);
            case "VisString255":
            case "VISIBLE STRING":
                return CmsDataDefinition.ofVisibleString(count != null ? count : 255);
            case "Unicode255":
            case "UNICODE STRING":
                return CmsDataDefinition.ofUnicodeString(count != null ? count : 255);
            case "Struct":
                if (templates != null && typeRef != null) {
                    return resolveStructType(templates, typeRef);
                }
                return CmsDataDefinition.ofInt32();
            case "Enum":
                return CmsDataDefinition.ofInt32U();
            case "Quality":   return CmsDataDefinition.ofQuality();
            case "Timestamp": return CmsDataDefinition.ofUtcTime();
            case "Check":     return CmsDataDefinition.ofCheck();
            case "Dbpos":     return CmsDataDefinition.ofDbpos();
            case "Tcmd":      return CmsDataDefinition.ofTcmd();
            default:
                log.warn("[Server] Unknown bType: {}", bType);
                return CmsDataDefinition.ofInt32();
        }
    }

    private CmsDataDefinition resolveStructType(SclDataTypeTemplates templates, String typeRef) {
        SclDAType daType = templates.findDaTypeById(typeRef);
        if (daType == null) return CmsDataDefinition.ofInt32();
        List<CmsDataDefinition.StructureEntry> bdaEntries = new ArrayList<>();
        for (SclBDA bda : daType.getBdas()) {
            CmsDataDefinition bdaDef = resolveBType(templates, bda.getBType(), bda.getType(), bda.getCount());
            if (bdaDef != null) {
                bdaEntries.add(new CmsDataDefinition.StructureEntry(bda.getName(), bdaDef));
            }
        }
        return CmsDataDefinition.ofStructure(bdaEntries);
    }

    private List<SclDO> getDosForType(SclDataTypeTemplates templates, String lnType) {
        if (templates == null || lnType == null || lnType.isEmpty()) {
            return null;
        }
        SclLNodeType lnt = templates.findLNodeTypeById(lnType);
        return lnt != null ? lnt.getDos() : null;
    }

    private List<TargetLn> resolveTargets(String ldName, String lnRef) {
        if (ldName != null && !ldName.isEmpty()) {
            SclLDevice device = findLDevice(server, ldName);
            if (device == null) {
                log.warn("[Server] LDevice not found: {}", ldName);
                return null;
            }
            List<TargetLn> result = new ArrayList<>();
            SclLN0 ln0 = device.getLn0();
            if (ln0 != null) {
                result.add(new TargetLn(ldName, "", ln0.getLnClass(), ln0.getInst(), ln0.getLnType(), ln0, null));
            }
            for (SclLN ln : device.getLns()) {
                result.add(new TargetLn(ldName, ln.getPrefix(), ln.getLnClass(), ln.getInst(), ln.getLnType(), null, ln));
            }
            return result;
        }
        if (lnRef == null || lnRef.isEmpty()) {
            log.warn("[Server] No ldName or lnReference provided");
            return null;
        }
        int slashIdx = lnRef.indexOf('/');
        if (slashIdx < 0) {
            log.warn("[Server] Invalid lnReference (no '/'): {}", lnRef);
            return null;
        }
        String targetLd = lnRef.substring(0, slashIdx);
        String targetLnName = lnRef.substring(slashIdx + 1);

        SclLDevice device = findLDevice(server, targetLd);
        if (device == null) {
            log.warn("[Server] LDevice not found: {}", targetLd);
            return null;
        }
        if (device.getLn0() != null) {
            String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
            if (ln0Name.equals(targetLnName)) {
                List<TargetLn> result = new ArrayList<>();
                result.add(new TargetLn(device.getInst(), "", device.getLn0().getLnClass(),
                        device.getLn0().getInst(), device.getLn0().getLnType(), device.getLn0(), null));
                return result;
            }
        }
        for (SclLN ln : device.getLns()) {
            String lnName = (ln.getPrefix() == null || ln.getPrefix().isEmpty())
                    ? ln.getLnClass() + ln.getInst()
                    : ln.getPrefix() + ln.getLnClass() + ln.getInst();
            if (lnName.equals(targetLnName)) {
                List<TargetLn> result = new ArrayList<>();
                result.add(new TargetLn(device.getInst(), ln.getPrefix(), ln.getLnClass(), ln.getInst(), ln.getLnType(), null, ln));
                return result;
            }
        }
        log.warn("[Server] LN not found: {} in LDevice {}. Available LNs:", targetLnName, targetLd);
        if (device.getLn0() != null) {
            log.warn("[Server]   {}", device.getLn0().getLnClass() + device.getLn0().getInst() + " (LN0)");
        }
        for (SclLN ln : device.getLns()) {
            String full = (ln.getPrefix() == null || ln.getPrefix().isEmpty())
                    ? ln.getLnClass() + ln.getInst()
                    : ln.getPrefix() + ln.getLnClass() + ln.getInst();
            log.warn("[Server]   {} (lnType={})", full, ln.getLnType());
        }
        return null;
    }

    private SclIED.SclLDevice findLDevice(SclIED.SclServer server, String ldName) {
        for (SclIED.SclLDevice ld : server.getLDevices()) {
            if (ld.getInst().equals(ldName)) {
                return ld;
            }
        }
        return null;
    }

    private static class TargetLn {
        final String ldInst;
        final String prefix;
        final String lnClass;
        final String inst;
        final String lnType;
        final SclLN0 ln0;
        final SclLN ln;

        TargetLn(String ldInst, String prefix, String lnClass, String inst, String lnType, SclLN0 ln0, SclLN ln) {
            this.ldInst = ldInst;
            this.prefix = prefix;
            this.lnClass = lnClass;
            this.inst = inst;
            this.lnType = lnType;
            this.ln0 = ln0;
            this.ln = ln;
        }

        String fullName() {
            return (prefix == null || prefix.isEmpty()) ? lnClass + inst : prefix + lnClass + inst;
        }
    }

    private static class DoEntry {
        final String ref;
        final String cdcType;
        final CmsDataDefinition definition;

        DoEntry(String ref, String cdcType, CmsDataDefinition definition) {
            this.ref = ref;
            this.cdcType = cdcType;
            this.definition = definition;
        }
    }
}
