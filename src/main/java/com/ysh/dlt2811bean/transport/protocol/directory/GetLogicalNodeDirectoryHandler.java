package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.scl.SclDocument;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates;
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
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalNodeDirectory;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GetLogicalNodeDirectoryHandler implements CmsServiceHandler {

    private final SclDocument sclDocument;

    public GetLogicalNodeDirectoryHandler(SclDocument sclDocument) {
        this.sclDocument = sclDocument;
    }

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_LOGIC_NODE_DIRECTORY;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsGetLogicalNodeDirectory asdu = (CmsGetLogicalNodeDirectory) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = session.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // Resolve reference: either ldName or lnReference
        String ldName = null;
        String lnRef = null;
        boolean useLdName = asdu.referenceRequest.getSelectedIndex() == 0;
        if (useLdName) {
            ldName = asdu.referenceRequest.ldName.get();
        } else {
            lnRef = asdu.referenceRequest.lnReference.get();
        }

        // Resolve target LNs
        List<TargetLn> targets = resolveTargets(accessPoint, ldName, lnRef);
        if (targets == null) {
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // Resolve acsiClass
        int acsiClass = asdu.acsiClass.get();

        // Collect SubReferences based on acsiClass
        List<String> entries = new ArrayList<>();
        switch (acsiClass) {
            case CmsACSIClass.DATA_OBJECT:
                collectDataObjects(targets, entries, !useLdName);
                break;
            case CmsACSIClass.DATA_SET:
                collectDataSets(targets, entries);
                break;
            case CmsACSIClass.BRCB:
                collectReportControls(targets, entries, true);
                break;
            case CmsACSIClass.URCB:
                collectReportControls(targets, entries, false);
                break;
            case CmsACSIClass.LCB:
                collectLogControls(targets, entries);
                break;
            case CmsACSIClass.LOG:
                collectLogNames(targets, entries);
                break;
            case CmsACSIClass.GO_CB:
                collectGseControls(targets, entries);
                break;
            case CmsACSIClass.MSV_CB:
                collectSvControls(targets, entries);
                break;
            case CmsACSIClass.SGCB:
                log.warn("[Server] ACSI class SGCB not supported in current SCL model");
                return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            default:
                log.warn("[Server] Unknown ACSI class: {}", acsiClass);
                return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        // Handle referenceAfter pagination
        String after = asdu.referenceAfter != null ? asdu.referenceAfter.get() : null;
        int startIndex = 0;
        if (after != null && !after.isEmpty()) {
            boolean found = false;
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).equals(after)) {
                    startIndex = i + 1;
                    found = true;
                    break;
                }
            }
            if (!found) {
                log.warn("[Server] referenceAfter not found: {}", after);
                return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
        }

        CmsArray<CmsSubReference> refs = new CmsArray<>(CmsSubReference::new).capacity(entries.size() - startIndex + 10);
        for (int i = startIndex; i < entries.size(); i++) {
            refs.add(new CmsSubReference(entries.get(i)));
        }

        CmsGetLogicalNodeDirectory response = new CmsGetLogicalNodeDirectory(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .referenceResponse(refs);
        response.moreFollows.set(false);

        log.info("[Server] GetLogicalNodeDirectory: {} entries (acsiClass={})", refs.size(), acsiClass);
        return new CmsApdu(response);
    }

    private List<TargetLn> resolveTargets(SclIED.SclAccessPoint accessPoint, String ldName, String lnRef) {
        SclIED.SclServer server = accessPoint.getServer();

        if (ldName != null && !ldName.isEmpty()) {
            // ldName: iterate all LNs in the specified LD
            SclIED.SclLDevice device = findLDevice(server, ldName);
            if (device == null) {
                log.warn("[Server] LDevice not found: {}", ldName);
                return null;
            }
            List<TargetLn> result = new ArrayList<>();
            SclLN0 ln0 = device.getLn0();
            if (ln0 != null) {
                result.add(new TargetLn(ldName, ln0.getLnClass(), ln0.getInst(), ln0.getLnType(), ln0, null));
            }
            for (SclLN ln : device.getLns()) {
                result.add(new TargetLn(ldName, ln.getLnClass(), ln.getInst(), ln.getLnType(), null, ln));
            }
            return result;
        }

        // lnReference: find specific LN by "LDInst/LNClassInst"
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

        SclIED.SclLDevice device = findLDevice(server, targetLd);
        if (device == null) {
            log.warn("[Server] LDevice not found: {}", targetLd);
            return null;
        }

        // Find LN where lnClass+inst matches targetLnName
        if (device.getLn0() != null) {
            String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
            if (ln0Name.equals(targetLnName)) {
                List<TargetLn> result = new ArrayList<>();
                result.add(new TargetLn(device.getInst(), device.getLn0().getLnClass(),
                        device.getLn0().getInst(), device.getLn0().getLnType(), device.getLn0(), null));
                return result;
            }
        }
        for (SclLN ln : device.getLns()) {
            String lnName = ln.getLnClass() + ln.getInst();
            if (lnName.equals(targetLnName)) {
                List<TargetLn> result = new ArrayList<>();
                result.add(new TargetLn(device.getInst(), ln.getLnClass(), ln.getInst(), ln.getLnType(), null, ln));
                return result;
            }
        }

        log.warn("[Server] LN not found: {} in LDevice {}", targetLnName, targetLd);
        return null;
    }

    private void collectDataObjects(List<TargetLn> targets, List<String> entries, boolean relative) {
        SclDataTypeTemplates templates = sclDocument != null ? sclDocument.getDataTypeTemplates() : null;

        for (TargetLn target : targets) {
            String prefix = relative ? "" : (target.lnClass + target.inst + ".");

            // Try to get DOs from LNodeType in DataTypeTemplates
            List<SclDO> dos = null;
            if (templates != null && target.lnType != null && !target.lnType.isEmpty()) {
                SclLNodeType lnt = templates.findLNodeTypeById(target.lnType);
                if (lnt != null) {
                    dos = lnt.getDos();
                }
            }

            if (dos != null && !dos.isEmpty()) {
                for (SclDO doDef : dos) {
                    String doPrefix = prefix + doDef.getName();
                    entries.add(doPrefix);
                    // Resolve SDOs from DOType
                    if (templates != null && doDef.getType() != null && !doDef.getType().isEmpty()) {
                        collectSdos(templates, doDef.getType(), doPrefix, entries);
                    }
                }
            }
        }
    }

    private void collectSdos(SclDataTypeTemplates templates, String doTypeId, String prefix, List<String> entries) {
        SclDOType doType = templates.findDoTypeById(doTypeId);
        if (doType == null) {
            return;
        }
        for (SclSDO sdo : doType.getSdos()) {
            String sdoPrefix = prefix + "." + sdo.getName();
            entries.add(sdoPrefix);
            // Recursively resolve nested SDOs
            if (sdo.getType() != null && !sdo.getType().isEmpty()) {
                collectSdos(templates, sdo.getType(), sdoPrefix, entries);
            }
        }
    }

    private void collectDataSets(List<TargetLn> targets, List<String> entries) {
        for (TargetLn t : targets) {
            if (t.ln0 == null) continue;
            for (SclIED.SclDataSet ds : t.ln0.getDataSets()) {
                entries.add(ds.getName());
            }
        }
    }

    private void collectReportControls(List<TargetLn> targets, List<String> entries, boolean buffered) {
        for (TargetLn t : targets) {
            if (t.ln0 == null) continue;
            for (SclIED.SclReportControl rc : t.ln0.getReportControls()) {
                if (rc.isBuffered() == buffered) {
                    entries.add(rc.getName());
                }
            }
        }
    }

    private void collectLogControls(List<TargetLn> targets, List<String> entries) {
        for (TargetLn t : targets) {
            if (t.ln0 == null) continue;
            for (SclIED.SclLogControl lc : t.ln0.getLogControls()) {
                entries.add(lc.getName());
            }
        }
    }

    private void collectLogNames(List<TargetLn> targets, List<String> entries) {
        for (TargetLn t : targets) {
            if (t.ln0 == null) continue;
            for (SclIED.SclLogControl lc : t.ln0.getLogControls()) {
                if (lc.getLogName() != null && !lc.getLogName().isEmpty()) {
                    entries.add(lc.getLogName());
                }
            }
        }
    }

    private void collectGseControls(List<TargetLn> targets, List<String> entries) {
        for (TargetLn t : targets) {
            if (t.ln0 == null) continue;
            for (SclIED.SclGSEControl gse : t.ln0.getGseControls()) {
                entries.add(gse.getName());
            }
        }
    }

    private void collectSvControls(List<TargetLn> targets, List<String> entries) {
        for (TargetLn t : targets) {
            if (t.ln0 == null) continue;
            for (SclIED.SclSampledValueControl sv : t.ln0.getSampledValueControls()) {
                entries.add(sv.getName());
            }
        }
    }

    private SclIED.SclLDevice findLDevice(SclIED.SclServer server, String ldName) {
        for (SclIED.SclLDevice ld : server.getLDevices()) {
            if (ld.getInst().equals(ldName)) {
                return ld;
            }
        }
        return null;
    }

    private CmsApdu buildNegativeResponse(CmsGetLogicalNodeDirectory request, int errorCode) {
        CmsGetLogicalNodeDirectory response = new CmsGetLogicalNodeDirectory(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get());
        response.serviceError.set(errorCode);
        return new CmsApdu(response);
    }

    private static class TargetLn {
        final String ldInst;
        final String lnClass;
        final String inst;
        final String lnType;
        final SclLN0 ln0;
        final SclLN ln;

        TargetLn(String ldInst, String lnClass, String inst, String lnType, SclLN0 ln0, SclLN ln) {
            this.ldInst = ldInst;
            this.lnClass = lnClass;
            this.inst = inst;
            this.lnType = lnType;
            this.ln0 = ln0;
            this.ln = ln;
        }
    }
}