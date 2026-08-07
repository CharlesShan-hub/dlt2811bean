package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.ServiceException;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.directory.CmsDataValueEntry;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesError;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesRequest;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesResponse;
import com.ysh.jcms.data.choice.CmsReferenceChoice;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.service.SclDirectoryService;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.List;

public class AllDataValuesServer extends BaseServerHandler<CmsGetAllDataValuesRequest, CmsGetAllDataValuesError> {

    public AllDataValuesServer() {
        super(ServiceName.GET_ALL_DATA_VALUES, CmsGetAllDataValuesRequest.class, CmsGetAllDataValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetAllDataValuesRequest req, int reqId) {
        String refAfter = req.isPresent("referenceAfter") ? req.referenceAfter.value() : null;
        log.info("GetAllDataValues from {}: reqId={}, refAfter={}", session.getSessionId(), reqId, refAfter);

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);
        SclAccessPoint ap = requireAp(session, reqId);

        String ldName = null, lnReference = null;
        if (req.reference.choice() == CmsReferenceChoice.LD_NAME)
            ldName = req.reference.altLdName.value();
        else if (req.reference.choice() == CmsReferenceChoice.LN_REFERENCE)
            lnReference = str(req.reference.altLnReference);

        List<SclLN> lns = Navigator.resolveLns(ied, ap, ldName, lnReference);
        if (lns == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String fcFilter = fcCode(req.isPresent("fc") ? req.fc.value() : -1);

        // Get all entries from service
        List<CmsDataValueEntry> allEntries = SclDirectoryService.getAllDataValues(doc, ied, lns, fcFilter);

        // Apply referenceAfter pagination
        List<CmsDataValueEntry> entries = afterEntries(allEntries, refAfter, reqId);

        // Apply pageSize
        int ps = pageSize();
        boolean more = entries.size() > ps;
        int limit = more ? ps : entries.size();

        CmsGetAllDataValuesResponse resp = new CmsGetAllDataValuesResponse();
        for (int i = 0; i < limit; i++)
            resp.data.add(entries.get(i));
        resp.moreFollows(more);
        log.info("GetAllDataValues: returning {} entries (total={}, moreFollows={})", limit, allEntries.size(), more);
        return ok(resp, reqId);
    }

    /** Apply referenceAfter pagination to a list of CmsDataValueEntry. */
    private static List<CmsDataValueEntry> afterEntries(List<CmsDataValueEntry> items, String refAfter, int reqId) {
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
}
