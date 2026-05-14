package com.ysh.dlt2811bean.transport.protocol.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.scl.SclTypeResolver;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDA;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDefinition;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataDefinitionEntry;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.ArrayList;
import java.util.List;

public class GetDataDefinitionHandler extends AbstractCmsServiceHandler<CmsGetDataDefinition> {

    private static final int MAX_ENTRIES_PER_RESPONSE = 50;

    public GetDataDefinitionHandler() {
        super(ServiceName.GET_DATA_DEFINITION, CmsGetDataDefinition::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        SclDataTypeTemplates templates = serverSession.getSclDataTypeTemplates();

        CmsArray<CmsGetDataDefinitionEntry> definitions = new CmsArray<>(CmsGetDataDefinitionEntry::new);
        int processedCount = 0;

        for (int i = 0; i < asdu.data.size(); i++) {
            if (processedCount >= MAX_ENTRIES_PER_RESPONSE) {
                break;
            }

            CmsGetDataValuesEntry entry = asdu.data.get(i);
            String ref = entry.reference.get();
            String fc = entry.fc.get();

            if (ref == null || ref.isEmpty()) {
                definitions.add(buildErrorEntry());
                processedCount++;
                continue;
            }

            CmsGetDataDefinitionEntry defEntry = buildDefinition(server, templates, ref, fc);
            definitions.add(defEntry);
            processedCount++;
        }

        boolean moreFollows = processedCount < asdu.data.size();

        CmsGetDataDefinition response = new CmsGetDataDefinition(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.definition = definitions;
        response.moreFollows.set(moreFollows);

        log.debug("[Server] GetDataDefinition: {} entries, moreFollows={}", definitions.size(), moreFollows);
        return new CmsApdu(response);
    }

    private CmsGetDataDefinitionEntry buildErrorEntry() {
        CmsGetDataDefinitionEntry defEntry = new CmsGetDataDefinitionEntry();
        CmsDataDefinition def = new CmsDataDefinition();
        def.set(CmsDataDefinition.ERROR);
        return defEntry.definition(def);
    }

    private CmsGetDataDefinitionEntry buildDefinition(SclIED.SclServer server, SclDataTypeTemplates templates,
                                                        String ref, String fc) {
        CmsGetDataDefinitionEntry defEntry = new CmsGetDataDefinitionEntry();

        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) {
            return defEntry.definition(buildErrorDef());
        }

        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        String[] parts = rest.split("\\.");

        SclIED.SclLDevice device = findLDevice(server, ldName);
        if (device == null || parts.length < 1) {
            return defEntry.definition(buildErrorDef());
        }

        String lnName = parts[0];
        boolean hasDo = parts.length > 1;

        if (!hasDo) {
            return defEntry.definition(buildErrorDef());
        }

        String doName = parts[1];
        //SclIED.SclDOI doi = findDoiInDevice(device, lnName, doName);

        if (fc != null && !fc.isEmpty() && !"XX".equals(fc)) {
            if (templates == null) {
                return defEntry.definition(buildErrorDef());
            }
            if (parts.length > 2) {
                String daName = parts[parts.length - 1];
                String daFc = SclTypeResolver.resolveFc(server, templates, ldName, lnName, doName, daName);
                if (daFc == null || !daFc.equals(fc)) {
                    return defEntry.definition(buildErrorDef());
                }
            } else {
                List<SclDA> das = SclTypeResolver.listDasFromType(server, templates, ldName, lnName, doName);
                boolean hasFc = false;
                for (SclDA da : das) {
                    if (fc.equals(da.getFc())) {
                        hasFc = true;
                        break;
                    }
                }
                if (!hasFc) {
                    return defEntry.definition(buildErrorDef());
                }
            }
        }

        boolean isDataAttribute = parts.length > 2;

        if (isDataAttribute) {
            defEntry.cdcType("");

            String daName = parts[parts.length - 1];
            if (parts.length == 3) {
                // Always use type template for bType
                if (templates != null) {
                    defEntry.definition(buildDaDefinitionFromType(server, templates, ldName, lnName, doName, daName));
                } else {
                    defEntry.definition(buildErrorDef());
                }
            } else if (parts.length == 4) {
                String sdiName = parts[2];
                // For SDI/DA (e.g. sVC.offset), use resolveSdiBType
                if (templates != null) {
                    String bType = SclTypeResolver.resolveSdiBType(server, templates, ldName, lnName, doName, sdiName, daName);
                    if (bType != null) {
                        defEntry.definition(bTypeToDataDefinition(bType));
                    } else {
                        defEntry.definition(buildErrorDef());
                    }
                } else {
                    defEntry.definition(buildErrorDef());
                }
            } else {
                defEntry.definition(buildErrorDef());
            }
        } else {
            String cdc = (templates != null)
                ? SclTypeResolver.resolveCdc(server, templates, ldName, lnName, doName)
                : "SPC";
            defEntry.cdcType(cdc != null ? cdc : "SPC");

            // Always use type template for DO definition
            if (templates != null) {
                defEntry.definition(buildDoDefinitionFromType(server, templates, ldName, lnName, doName));
            } else {
                defEntry.definition(buildErrorDef());
            }
        }

        return defEntry;
    }

    /**
     * Builds a DO definition from type templates (when no instance DOI exists).
     */
    private CmsDataDefinition buildDoDefinitionFromType(SclIED.SclServer server, SclDataTypeTemplates templates,
                                                         String ldName, String lnName, String doName) {
        List<SclDA> das = SclTypeResolver.listDasFromType(server, templates, ldName, lnName, doName);
        if (das == null || das.isEmpty()) {
            return buildErrorDef();
        }
        List<CmsDataDefinition.StructureEntry> entries = new ArrayList<>();
        for (SclDA da : das) {
            entries.add(new CmsDataDefinition.StructureEntry(
                    da.getName(), da.getFc(), bTypeToDataDefinition(da.getBType())));
        }
        // Also add SDOs
        SclDataTypeTemplates.SclDO doObj = SclTypeResolver.findDoInType(server, templates, ldName, lnName, doName);
        if (doObj != null) {
            SclDataTypeTemplates.SclDOType dot = templates.findDoTypeById(doObj.getType());
            if (dot != null) {
                for (SclDataTypeTemplates.SclSDO sdo : dot.getSdos()) {
                    entries.add(new CmsDataDefinition.StructureEntry(
                            sdo.getName(), CmsDataDefinition.ofBoolean()));
                }
            }
        }
        return CmsDataDefinition.ofStructure(entries);
    }

    /**
     * Builds a DA definition from type templates (when no instance DAI exists).
     */
    private CmsDataDefinition buildDaDefinitionFromType(SclIED.SclServer server, SclDataTypeTemplates templates,
                                                         String ldName, String lnName, String doName, String daName) {
        String bType = SclTypeResolver.resolveBType(server, templates, ldName, lnName, doName, daName);
        if (bType == null) {
            return buildErrorDef();
        }
        return bTypeToDataDefinition(bType);
    }

    /**
     * Converts an SCL bType string to a CmsDataDefinition.
     */
    private CmsDataDefinition bTypeToDataDefinition(String bType) {
        if (bType == null) return CmsDataDefinition.ofBoolean();
        switch (bType.toUpperCase()) {
            case "BOOLEAN": return CmsDataDefinition.ofBoolean();
            case "INT8": return CmsDataDefinition.ofInt8();
            case "INT16": return CmsDataDefinition.ofInt16();
            case "INT32": return CmsDataDefinition.ofInt32();
            case "INT64": return CmsDataDefinition.ofInt64();
            case "INT8U": return CmsDataDefinition.ofInt8U();
            case "INT16U": return CmsDataDefinition.ofInt16U();
            case "INT32U": return CmsDataDefinition.ofInt32U();
            case "INT64U": return CmsDataDefinition.ofInt64U();
            case "FLOAT32": return CmsDataDefinition.ofFloat32();
            case "FLOAT64": return CmsDataDefinition.ofFloat64();
            case "BIT_STRING":
            case "BITSTRING": return CmsDataDefinition.ofBitString(0);
            case "OCTET_STRING":
            case "OCTETSTRING": return CmsDataDefinition.ofOctetString(-255);
            case "VISSTRING255":
            case "VISIBLE_STRING": return CmsDataDefinition.ofVisibleString(-255);
            case "UNICODE_STRING":
            case "UNICODESTRING": return CmsDataDefinition.ofUnicodeString(-255);
            case "UTC_TIME":
            case "UTCTIME": return CmsDataDefinition.ofUtcTime();
            case "BINARY_TIME":
            case "BINARYTIME":
            case "ENTRYTIME": return CmsDataDefinition.ofBinaryTime();
            case "QUALITY": return CmsDataDefinition.ofQuality();
            case "DBPOS": return CmsDataDefinition.ofDbpos();
            case "TCMD": return CmsDataDefinition.ofTcmd();
            case "CHECK": return CmsDataDefinition.ofCheck();
            case "STRUCT": return CmsDataDefinition.ofBoolean(); // fallback for struct
            case "TIMESTAMP": return CmsDataDefinition.ofUtcTime();
            case "VISSTRING64": return CmsDataDefinition.ofVisibleString(-64);
            case "UNICODE255": return CmsDataDefinition.ofUnicodeString(-255);
            default:
                log.warn("[Server] Unknown bType: {}", bType);
                return CmsDataDefinition.ofBoolean();
        }
    }

    private CmsDataDefinition buildErrorDef() {
        CmsDataDefinition def = new CmsDataDefinition();
        def.set(CmsDataDefinition.ERROR);
        return def;
    }

    private CmsDataDefinition buildDoDefinition(SclIED.SclDOI doi) {
        List<CmsDataDefinition.StructureEntry> entries = new ArrayList<>();
        for (SclIED.SclDAI dai : doi.getDais()) {
            entries.add(new CmsDataDefinition.StructureEntry(
                    dai.getName(), CmsDataDefinition.ofBoolean()));
        }
        for (SclIED.SclSDI sdi : doi.getSdis()) {
            entries.add(new CmsDataDefinition.StructureEntry(
                    sdi.getName(), CmsDataDefinition.ofBoolean()));
        }
        return CmsDataDefinition.ofStructure(entries);
    }

    private CmsDataDefinition buildDaDefinition(java.util.List<SclIED.SclDAI> dais, String daName) {
        for (SclIED.SclDAI dai : dais) {
            if (dai.getName().equals(daName)) {
                return CmsDataDefinition.ofBoolean();
            }
        }
        return buildErrorDef();
    }

    private SclIED.SclSDI findSdi(SclIED.SclDOI doi, String sdiName) {
        for (SclIED.SclSDI sdi : doi.getSdis()) {
            if (sdi.getName().equals(sdiName)) return sdi;
        }
        return null;
    }

    private SclIED.SclDOI findDoiInDevice(SclIED.SclLDevice device, String lnName, String doName) {
        if (doName == null) return null;
        if (device.getLn0() != null) {
            String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
            if (ln0Name.equals(lnName)) {
                for (SclIED.SclDOI doi : device.getLn0().getDois()) {
                    if (doi.getName().equals(doName)) return doi;
                }
                return null;
            }
        }
        for (SclIED.SclLN ln : device.getLns()) {
            String curLnName = (ln.getPrefix() == null || ln.getPrefix().isEmpty())
                    ? ln.getLnClass() + ln.getInst()
                    : ln.getPrefix() + ln.getLnClass() + ln.getInst();
            if (curLnName.equals(lnName)) {
                for (SclIED.SclDOI doi : ln.getDois()) {
                    if (doi.getName().equals(doName)) return doi;
                }
                return null;
            }
        }
        return null;
    }

    private SclIED.SclLDevice findLDevice(SclIED.SclServer server, String ldName) {
        for (SclIED.SclLDevice device : server.getLDevices()) {
            if (device.getInst().equals(ldName)) return device;
        }
        return null;
    }
}
