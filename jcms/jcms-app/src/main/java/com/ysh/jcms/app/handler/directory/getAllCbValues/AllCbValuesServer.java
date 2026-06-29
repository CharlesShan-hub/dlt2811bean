package com.ysh.jcms.app.handler.directory.getAllCbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.directory.CmsAcsiClass;
import com.ysh.jcms.svc.directory.CmsCbValueChoice;
import com.ysh.jcms.svc.directory.CmsCbValueEntry;
import com.ysh.jcms.svc.directory.CmsGetAllCbValuesError;
import com.ysh.jcms.svc.directory.CmsGetAllCbValuesRequest;
import com.ysh.jcms.svc.directory.CmsGetAllCbValuesResponse;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.utils.scl.model.data.SclCBEntry;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.lnBuilder.SclLNControlBlockCollector;
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
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetAllCbValuesRequest req = (CmsGetAllCbValuesRequest) rawReq;
        int reqId = req.reqId.value();
        int acsiClass = req.acsiClass.value();
        String refAfter = req.refAfterPresent.value() && req.refAfter.len > 0
            ? new String(req.refAfter.value(), StandardCharsets.UTF_8) : null;

        log.info("GetAllCBValues from {}: reqId={}, acsiClass={}", session.getSessionId(), reqId, acsiClass);

        SclServer server = getSclServer(session);
        if (server == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ldName = null;
        String lnReference = null;
        if (req.reference.choice.value() == CmsReferenceChoice.LD_NAME) {
            ldName = req.reference.altLdName.len > 0
                ? new String(req.reference.altLdName.value(), StandardCharsets.UTF_8) : null;
        } else if (req.reference.choice.value() == CmsReferenceChoice.LN_REFERENCE) {
            lnReference = req.reference.altLnReference.len > 0
                ? new String(req.reference.altLnReference.value(), StandardCharsets.UTF_8) : null;
        }

        List<SclLN> lns = server.resolveLns(ldName, lnReference);
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
        outer:
        for (SclLN ln : lns) {
            List<SclCBEntry> cbEntries;
            try {
                cbEntries = SclLNControlBlockCollector.collectCBValues(ln, acsiClass);
            } catch (IllegalArgumentException e) {
                log.warn("GetAllCBValues: unsupported acsiClass={} for LN {}", acsiClass, ln.getFullName());
                continue;
            }

            for (SclCBEntry cb : cbEntries) {
                String fullRef = ln.getFullName() + "." + cb.ref;

                // referenceAfter pagination
                if (refAfter != null) {
                    if (fullRef.equals(refAfter)) {
                        refAfter = null;
                    }
                    continue;
                }

                CmsCbValueEntry entry = new CmsCbValueEntry()
                    .reference(fullRef)
                    .value((CmsCbValueChoice) cb.value);
                entries.add(entry);

                if (entries.size() >= pageSize) break outer;
            }
        }

        CmsGetAllCbValuesResponse resp = new CmsGetAllCbValuesResponse()
            .reqId(reqId);
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

    private static boolean isValidAcsiClass(int acsiClass) {
        return acsiClass == CmsAcsiClass.BRCB
            || acsiClass == CmsAcsiClass.URCB
            || acsiClass == CmsAcsiClass.LCB
            || acsiClass == CmsAcsiClass.SGECB
            || acsiClass == CmsAcsiClass.GOCB
            || acsiClass == CmsAcsiClass.MSVCB;
    }
}
