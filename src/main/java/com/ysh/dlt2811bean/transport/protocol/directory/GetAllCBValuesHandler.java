package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.compound.CmsBRCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsGoCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsLCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsMSVCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsSGCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsURCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.scl.model.SclIED.SclGSEControl;
import com.ysh.dlt2811bean.scl.model.SclIED.SclLN;
import com.ysh.dlt2811bean.scl.model.SclIED.SclLN0;
import com.ysh.dlt2811bean.scl.model.SclIED.SclLogControl;
import com.ysh.dlt2811bean.scl.model.SclIED.SclReportControl;
import com.ysh.dlt2811bean.scl.model.SclIED.SclSampledValueControl;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllCBValues;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsCBValue;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsCBValueEntry;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GetAllCBValuesHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_ALL_CB_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling GetAllCBValues: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsGetAllCBValues) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsGetAllCBValues asdu = (CmsGetAllCBValues) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = session.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ldName = null;
        String lnRef = null;
        boolean useLdName = asdu.reference.getSelectedIndex() == 0;
        if (useLdName) {
            ldName = asdu.reference.ldName.get();
        } else {
            lnRef = asdu.reference.lnReference.get();
        }

        int acsiClass = asdu.acsiClass.get();

        List<SclLN0> ln0s = resolveLn0s(accessPoint, ldName, lnRef);
        if (ln0s == null) {
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        List<CbEntry> entries = collectCBs(ln0s, acsiClass);

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
                return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
        }

        CmsArray<CmsCBValueEntry> cbValue = new CmsArray<>(CmsCBValueEntry::new)
                .capacity(Math.max(1, entries.size() - startIndex));
        for (int i = startIndex; i < entries.size(); i++) {
            cbValue.add(entries.get(i).entry);
        }

        CmsGetAllCBValues response = new CmsGetAllCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .cbValue(cbValue);
        response.moreFollows.set(false);

        log.info("[Server] GetAllCBValues: {} entries (acsiClass={})", cbValue.size(), acsiClass);
        return new CmsApdu(response);
    }

    private List<SclLN0> resolveLn0s(SclIED.SclAccessPoint accessPoint, String ldName, String lnRef) {
        SclIED.SclServer server = accessPoint.getServer();
        if (ldName != null && !ldName.isEmpty()) {
            SclIED.SclLDevice device = findLDevice(server, ldName);
            if (device == null) {
                log.warn("[Server] LDevice not found: {}", ldName);
                return null;
            }
            List<SclLN0> result = new ArrayList<>();
            if (device.getLn0() != null) {
                result.add(device.getLn0());
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
        SclIED.SclLDevice device = findLDevice(server, targetLd);
        if (device == null) {
            log.warn("[Server] LDevice not found: {}", targetLd);
            return null;
        }
        if (device.getLn0() != null) {
            String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
            if (ln0Name.equals(targetLnName)) {
                List<SclLN0> result = new ArrayList<>();
                result.add(device.getLn0());
                return result;
            }
        }
        log.warn("[Server] LN0 not found for reference: {}", lnRef);
        return null;
    }

    private List<CbEntry> collectCBs(List<SclLN0> ln0s, int acsiClass) {
        List<CbEntry> result = new ArrayList<>();
        for (SclLN0 ln0 : ln0s) {
            switch (acsiClass) {
                case CmsACSIClass.BRCB:
                    for (SclReportControl rc : ln0.getReportControls()) {
                        if (rc.isBuffered()) {
                            result.add(new CbEntry(rc.getName(), buildBrcb(rc)));
                        }
                    }
                    break;
                case CmsACSIClass.URCB:
                    for (SclReportControl rc : ln0.getReportControls()) {
                        if (!rc.isBuffered()) {
                            result.add(new CbEntry(rc.getName(), buildUrcb(rc)));
                        }
                    }
                    break;
                case CmsACSIClass.LCB:
                    for (SclLogControl lc : ln0.getLogControls()) {
                        result.add(new CbEntry(lc.getName(), buildLcb(lc)));
                    }
                    break;
                case CmsACSIClass.GO_CB:
                    for (SclGSEControl gse : ln0.getGseControls()) {
                        result.add(new CbEntry(gse.getName(), buildGocb(gse)));
                    }
                    break;
                case CmsACSIClass.MSV_CB:
                    for (SclSampledValueControl sv : ln0.getSampledValueControls()) {
                        result.add(new CbEntry(sv.getName(), buildMsvcb(sv)));
                    }
                    break;
                case CmsACSIClass.SGCB:
                    result.add(new CbEntry("SG1", buildSgcb(null)));
                    break;
                default:
                    log.warn("[Server] Unsupported ACSI class for CB: {}", acsiClass);
                    break;
            }
        }
        return result;
    }

    private CmsCBValue buildBrcb(SclReportControl rc) {
        CmsBRCB brcb = new CmsBRCB();
        brcb.brcbName.set(rc.getName());
        if (rc.getDatSet() != null) {
            brcb.datSet.set(rc.getDatSet());
        }
        if (rc.getRptID() != null) {
            brcb.rptID.set(rc.getRptID());
        }
        if (rc.getConfRev() != null) {
            brcb.confRev.set(Long.parseLong(rc.getConfRev()));
        }
        return new CmsCBValue().selectBrcb();
    }

    private CmsCBValue buildUrcb(SclReportControl rc) {
        CmsURCB urcb = new CmsURCB();
        urcb.urcbName.set(rc.getName());
        if (rc.getDatSet() != null) {
            urcb.datSet.set(rc.getDatSet());
        }
        if (rc.getRptID() != null) {
            urcb.rptID.set(rc.getRptID());
        }
        if (rc.getConfRev() != null) {
            urcb.confRev.set(Long.parseLong(rc.getConfRev()));
        }
        return new CmsCBValue().selectUrcb();
    }

    private CmsCBValue buildLcb(SclLogControl lc) {
        CmsLCB lcb = new CmsLCB();
        lcb.lcbName.set(lc.getName());
        if (lc.getDatSet() != null) {
            lcb.datSet.set(lc.getDatSet());
        }
        if (lc.getLogName() != null) {
            lcb.logRef.set(lc.getLogName());
        }
        return new CmsCBValue().selectLcb();
    }

    private CmsCBValue buildGocb(SclGSEControl gse) {
        CmsGoCB gocb = new CmsGoCB();
        gocb.goCBName.set(gse.getName());
        if (gse.getDatSet() != null) {
            gocb.datSet.set(gse.getDatSet());
        }
        if (gse.getAppID() != null) {
            gocb.goID.set(gse.getAppID());
        }
        if (gse.getConfRev() != null) {
            gocb.confRev.set(Long.parseLong(gse.getConfRev()));
        }
        return new CmsCBValue().selectGocb();
    }

    private CmsCBValue buildMsvcb(SclSampledValueControl sv) {
        CmsMSVCB msvcb = new CmsMSVCB();
        msvcb.msvCBName.set(sv.getName());
        if (sv.getDatSet() != null) {
            msvcb.datSet.set(sv.getDatSet());
        }
        if (sv.getSmvID() != null) {
            msvcb.msvID.set(sv.getSmvID());
        }
        if (sv.getConfRev() != null) {
            msvcb.confRev.set(Long.parseLong(sv.getConfRev()));
        }
        if (sv.getSmpRate() > 0) {
            msvcb.smpRate.set(sv.getSmpRate());
        }
        return new CmsCBValue().selectMsvcb();
    }

    private CmsCBValue buildSgcb(String dummy) {
        CmsSGCB sgb = new CmsSGCB();
        sgb.sgcbName.set("SG1");
        return new CmsCBValue().selectSgb();
    }

    private SclIED.SclLDevice findLDevice(SclIED.SclServer server, String ldName) {
        for (SclIED.SclLDevice ld : server.getLDevices()) {
            if (ld.getInst().equals(ldName)) {
                return ld;
            }
        }
        return null;
    }

    private CmsApdu buildNegativeResponse(CmsGetAllCBValues request, int errorCode) {
        CmsGetAllCBValues response = new CmsGetAllCBValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get());
        response.serviceError.set(errorCode);
        return new CmsApdu(response);
    }

    private static class CbEntry {
        final String ref;
        final CmsCBValueEntry entry;

        CbEntry(String name, CmsCBValue value) {
            this.ref = name;
            this.entry = new CmsCBValueEntry().reference(name);
            this.entry.value = value;
        }
    }
}
