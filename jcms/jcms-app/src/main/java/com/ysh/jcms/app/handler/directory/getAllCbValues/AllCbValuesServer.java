package com.ysh.jcms.app.handler.directory.getAllCbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.ServiceException;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.enumerate.CmsAcsiClass;
import com.ysh.jcms.data.sequence.directory.CmsCbValueEntry;
import com.ysh.jcms.pdu.directory.CmsGetAllCbValuesError;
import com.ysh.jcms.pdu.directory.CmsGetAllCbValuesRequest;
import com.ysh.jcms.pdu.directory.CmsGetAllCbValuesResponse;
import com.ysh.jcms.data.choice.CmsReferenceChoice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.service.SclDirectoryService;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.List;

public class AllCbValuesServer extends BaseServerHandler<CmsGetAllCbValuesRequest, CmsGetAllCbValuesError> {

    public AllCbValuesServer() {
        super(ServiceName.GET_ALL_CB_VALUES, CmsGetAllCbValuesRequest.class, CmsGetAllCbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetAllCbValuesRequest req, int reqId) {
        int acsiClass = req.acsiClass.value();
        String refAfter = req.isPresent("referenceAfter") ? req.referenceAfter.value() : null;

        log.info("GetAllCBValues from {}: reqId={}, acsiClass={}", session.getSessionId(), reqId, acsiClass);

        SclIED ied = requireIed(session, reqId);
        SclAccessPoint ap = requireAp(session, reqId);

        // Validate ACSI class
        if (!isValidAcsiClass(acsiClass)) {
            log.warn("GetAllCBValues: invalid acsiClass={}", acsiClass);
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        String ldName = null;
        String lnReference = null;
        if (req.reference.choice() == CmsReferenceChoice.LD_NAME) {
            ldName = req.reference.altLdName.value();
        } else if (req.reference.choice() == CmsReferenceChoice.LN_REFERENCE) {
            lnReference = req.reference.altLnReference.value();
        }

        List<SclLN> lns = Navigator.resolveLns(ied, ap, ldName, lnReference);
        if (lns == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // Get all entries from service
        List<CmsCbValueEntry> allEntries = SclDirectoryService.getAllCbValues(lns, acsiClass);

        // Apply referenceAfter pagination
        List<CmsCbValueEntry> entries = afterEntries(allEntries, refAfter, reqId);

        // Apply pageSize
        int ps = pageSize();
        boolean more = entries.size() > ps;
        int limit = more ? ps : entries.size();

        CmsGetAllCbValuesResponse resp = new CmsGetAllCbValuesResponse();
        for (int i = 0; i < limit; i++) {
            resp.cbValue.add(entries.get(i));
        }
        resp.moreFollows(more);

        log.info("GetAllCBValues: returning {} entries", limit);

        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode GetAllCBValuesResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    /** Apply referenceAfter pagination to a list of CmsCbValueEntry. */
    private static List<CmsCbValueEntry> afterEntries(List<CmsCbValueEntry> items, String refAfter, int reqId) {
        if (refAfter == null || refAfter.isEmpty())
            return items;
        for (int i = 0; i < items.size(); i++) {
            String ref = items.get(i).reference.value();
            if (refAfter.equals(ref)) {
                return items.subList(i + 1, items.size());
            }
        }
        throw new ServiceException(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
    }

    private static boolean isValidAcsiClass(int acsiClass) {
        return acsiClass == CmsAcsiClass.BRCB || acsiClass == CmsAcsiClass.URCB || acsiClass == CmsAcsiClass.LCB
                || acsiClass == CmsAcsiClass.SGCB || acsiClass == CmsAcsiClass.GOCB || acsiClass == CmsAcsiClass.MSVCB;
    }
}
