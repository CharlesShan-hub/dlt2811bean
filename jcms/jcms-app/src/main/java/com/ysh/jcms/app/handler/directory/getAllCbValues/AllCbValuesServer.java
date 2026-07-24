package com.ysh.jcms.app.handler.directory.getAllCbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.directory.CmsAcsiClass;
import com.ysh.jcms.svc.directory.CmsCbValueChoice;
import com.ysh.jcms.svc.directory.CmsCbValueEntry;
import com.ysh.jcms.svc.directory.CmsGetAllCbValuesError;
import com.ysh.jcms.svc.directory.CmsGetAllCbValuesRequest;
import com.ysh.jcms.svc.directory.CmsGetAllCbValuesResponse;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.convert.CbConverter;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AllCbValuesServer extends BaseServerHandler {

    public AllCbValuesServer() {
        super(ServiceName.GET_ALL_CB_VALUES, CmsGetAllCbValuesRequest.class, CmsGetAllCbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsGetAllCbValuesRequest req = (CmsGetAllCbValuesRequest) rawReq;
        int acsiClass = req.acsiClass.value();
        String refAfter = req.refAfterPresent.value() && req.refAfter.len > 0
                ? new String(req.refAfter.value(), StandardCharsets.UTF_8)
                : null;

        log.info("GetAllCBValues from {}: reqId={}, acsiClass={}", session.getSessionId(), reqId, acsiClass);

        SclDocument doc = requireScl(session, reqId);

        String ldName = null;
        String lnReference = null;
        if (req.reference.choice.value() == CmsReferenceChoice.LD_NAME) {
            ldName = req.reference.altLdName.len > 0 ? new String(req.reference.altLdName.value(), StandardCharsets.UTF_8) : null;
        } else if (req.reference.choice.value() == CmsReferenceChoice.LN_REFERENCE) {
            lnReference = req.reference.altLnReference.len > 0
                    ? new String(req.reference.altLnReference.value(), StandardCharsets.UTF_8)
                    : null;
        }

        List<SclLN> lns = resolveLns(doc, ldName, lnReference);
        if (lns == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // Validate ACSI class
        if (!isValidAcsiClass(acsiClass)) {
            log.warn("GetAllCBValues: invalid acsiClass={}", acsiClass);
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        // Collect CB entries
        List<CmsCbValueEntry> entries = new ArrayList<>();
        int pageSize = pageSize();
        outer : for (SclLN ln : lns) {
            List<CbPair> cbPairs = collectCbValues(ln, acsiClass);

            for (CbPair cb : cbPairs) {
                String fullRef = ln.getFullName() + "." + cb.ref;

                // referenceAfter pagination
                if (refAfter != null) {
                    if (fullRef.equals(refAfter)) {
                        refAfter = null;
                    }
                    continue;
                }

                entries.add(new CmsCbValueEntry().reference(fullRef).value(cb.value));

                if (entries.size() >= pageSize)
                    break outer;
            }
        }

        CmsGetAllCbValuesResponse resp = new CmsGetAllCbValuesResponse().reqId(reqId);
        for (CmsCbValueEntry e : entries) {
            resp.cbValue.add(e);
        }
        resp.moreFollows(entries.size() >= pageSize());

        log.info("GetAllCBValues: returning {} entries", entries.size());

        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode GetAllCBValuesResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    /** Resolve LNs across all IEDs. */
    private static List<SclLN> resolveLns(SclDocument doc, String ldName, String lnReference) {
        List<SclLN> result = new ArrayList<>();
        if (ldName != null && !ldName.isEmpty()) {
            for (SclIED ied : doc.ieds()) {
                for (SclAccessPoint ap : ied.accessPoints()) {
                    SclServer srv = ap.server();
                    if (srv != null) {
                        SclLDevice device = srv.findLDeviceByInst(ldName);
                        if (device != null) {
                            result.addAll(device.lns());
                            return result;
                        }
                    }
                }
            }
            return null;
        }
        if (lnReference == null || lnReference.isEmpty())
            return null;
        int slashIdx = lnReference.indexOf('/');
        if (slashIdx < 0)
            return null;
        String refLd = lnReference.substring(0, slashIdx);
        String refLn = lnReference.substring(slashIdx + 1);
        for (SclIED ied : doc.ieds()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv != null) {
                    SclLDevice device = srv.findLDeviceByInst(refLd);
                    if (device != null) {
                        SclLN ln = device.findLnByFullName(refLn);
                        if (ln != null) {
                            result.add(ln);
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    }

    /** Simple pair of CB ref string and CmsCbValueChoice. */
    private static final class CbPair {
        final String ref;
        final CmsCbValueChoice value;
        CbPair(String ref, CmsCbValueChoice value) {
            this.ref = ref;
            this.value = value;
        }
    }

    /** Collect CB value entries for a given LN and ACSI class. */
    private static List<CbPair> collectCbValues(SclLN ln, int acsiClass) {
        List<CbPair> result = new ArrayList<>();
        switch (acsiClass) {
            case CmsAcsiClass.BRCB :
                for (SclReportControl rc : ln.reportControls()) {
                    if ("true".equals(rc.buffered())) {
                        result.add(new CbPair(rc.name(), CbConverter.brcbFrom(rc)));
                    }
                }
                break;
            case CmsAcsiClass.URCB :
                for (SclReportControl rc : ln.reportControls()) {
                    if (!"true".equals(rc.buffered())) {
                        result.add(new CbPair(rc.name(), CbConverter.urcbFrom(rc)));
                    }
                }
                break;
            case CmsAcsiClass.LCB :
                for (SclLogControl lc : ln.logControls()) {
                    result.add(new CbPair(lc.name(), CbConverter.lcbFrom(lc)));
                }
                break;
            case CmsAcsiClass.GOCB :
                for (SclGSEControl gc : ln.gseControls()) {
                    result.add(new CbPair(gc.name(), CbConverter.gocbFrom(gc)));
                }
                break;
            case CmsAcsiClass.MSVCB :
                for (SclSampledValueControl sv : ln.svControls()) {
                    result.add(new CbPair(sv.name(), CbConverter.msvcbFrom(sv)));
                }
                break;
            default :
                break;
        }
        return result;
    }

    private static boolean isValidAcsiClass(int acsiClass) {
        return acsiClass == CmsAcsiClass.BRCB || acsiClass == CmsAcsiClass.URCB || acsiClass == CmsAcsiClass.LCB
                || acsiClass == CmsAcsiClass.SGECB || acsiClass == CmsAcsiClass.GOCB || acsiClass == CmsAcsiClass.MSVCB;
    }
}
