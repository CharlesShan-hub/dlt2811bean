package com.ysh.jcms.app.handler.directory.getAllDataDefinition;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.ServiceException;
import com.ysh.jcms.data.choice.CmsReferenceChoice;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.directory.CmsDataDefinitionEntry;
import com.ysh.jcms.pdu.directory.CmsGetAllDataDefinitionError;
import com.ysh.jcms.pdu.directory.CmsGetAllDataDefinitionRequest;
import com.ysh.jcms.pdu.directory.CmsGetAllDataDefinitionResponse;
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

public class AllDataDefServer extends BaseServerHandler<CmsGetAllDataDefinitionRequest, CmsGetAllDataDefinitionError> {

    public AllDataDefServer() {
        super(ServiceName.GET_ALL_DATA_DEFINITION, CmsGetAllDataDefinitionRequest.class, CmsGetAllDataDefinitionError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetAllDataDefinitionRequest req, int reqId) {
        String refAfter = req.isPresent("referenceAfter") ? req.referenceAfter.value() : null;

        log.info("GetAllDataDefinition from {}: reqId={}", session.getSessionId(), reqId);

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);
        SclAccessPoint ap = requireAp(session, reqId);

        String ldName = null;
        String lnReference = null;
        if (req.reference.choice() == CmsReferenceChoice.LD_NAME) {
            ldName = req.reference.altLdName.value();
        } else if (req.reference.choice() == CmsReferenceChoice.LN_REFERENCE) {
            lnReference = str(req.reference.altLnReference);
        }

        List<SclLN> lns = Navigator.resolveLns(ied, ap, ldName, lnReference);
        if (lns == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // Resolve fc filter from request
        String fcFilter = fcCode(req.isPresent("fc") ? req.fc.value() : -1);

        // Get all entries from service
        List<CmsDataDefinitionEntry> allEntries = SclDirectoryService.getAllDataDefinition(doc, lns, fcFilter);

        // Apply referenceAfter pagination
        List<CmsDataDefinitionEntry> entries = afterEntries(allEntries, refAfter, reqId);

        // Apply pageSize
        int ps = pageSize();
        boolean more = entries.size() > ps;
        int limit = more ? ps : entries.size();

        CmsGetAllDataDefinitionResponse resp = new CmsGetAllDataDefinitionResponse();
        for (int i = 0; i < limit; i++) {
            resp.data.add(entries.get(i));
        }
        resp.moreFollows(more);

        log.info("GetAllDataDefinition: returning {} entries", limit);
        return ok(resp, reqId);
    }

    /** Apply referenceAfter pagination to a list of CmsDataDefinitionEntry. */
    private static List<CmsDataDefinitionEntry> afterEntries(List<CmsDataDefinitionEntry> items, String refAfter, int reqId) {
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
