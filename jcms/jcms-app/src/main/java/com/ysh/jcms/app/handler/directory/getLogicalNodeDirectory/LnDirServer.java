package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.scalar.CmsSubReference;
import com.ysh.jcms.core.pdu.directory.CmsGetLogicalNodeDirectoryError;
import com.ysh.jcms.core.pdu.directory.CmsGetLogicalNodeDirectoryRequest;
import com.ysh.jcms.core.pdu.directory.CmsGetLogicalNodeDirectoryResponse;
import com.ysh.jcms.core.data.choice.CmsReferenceChoice;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import com.ysh.jcms.utils.scl.service.SclDirectoryService;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.List;

public class LnDirServer extends BaseServerHandler<CmsGetLogicalNodeDirectoryRequest, CmsGetLogicalNodeDirectoryError> {

    public LnDirServer() {
        super(ServiceName.GET_LOGIC_NODE_DIRECTORY, CmsGetLogicalNodeDirectoryRequest.class, CmsGetLogicalNodeDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetLogicalNodeDirectoryRequest req, int reqId) {
        int acsiClass = req.acsiClass.value();
        String refAfter = req.isPresent("referenceAfter") ? req.referenceAfter.value() : null;

        log.info("GetLogicalNodeDirectory from {}: reqId={}, acsiClass={}", session.sessionId(), reqId, acsiClass);

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

        // 获取有效 LD 名：ldName 或从 lnReference 中提取
        String effectiveLd = ldName != null ? ldName : (lnReference != null ? SclRefParser.parse(lnReference).ldName() : null);

        List<String> names = SclDirectoryService.getLogicalNodeDirectory(doc, lns, effectiveLd, acsiClass);
        names = after(names, refAfter, reqId);

        CmsGetLogicalNodeDirectoryResponse resp = new CmsGetLogicalNodeDirectoryResponse();

        int pageSize = pageSize();
        boolean more = names.size() > pageSize;
        int limit = more ? pageSize : names.size();
        for (int i = 0; i < limit; i++) {
            resp.reference.add(new CmsSubReference(names.get(i)));
        }
        resp.moreFollows(more);

        return ok(resp, reqId);
    }
}
