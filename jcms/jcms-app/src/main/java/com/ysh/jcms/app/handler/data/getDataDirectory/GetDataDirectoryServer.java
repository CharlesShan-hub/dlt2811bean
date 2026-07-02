package com.ysh.jcms.app.handler.data.getDataDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.svc.data.CmsGetDataDirectoryError;
import com.ysh.jcms.svc.data.CmsGetDataDirectoryRequest;
import com.ysh.jcms.svc.data.CmsGetDataDirectoryResponse;
import com.ysh.jcms.svc.data.CmsSubRefEntry;
import com.ysh.jcms.utils.scl.model.data.SclDataDirectoryEntry;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.util.RefUtil;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetDataDirectoryServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetDataDirectoryServer.class);

    public GetDataDirectoryServer() {
        super(ServiceName.GET_DATA_DIRECTORY, CmsGetDataDirectoryRequest.class, CmsGetDataDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetDataDirectoryRequest req = (CmsGetDataDirectoryRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("GetDataDirectory from {}: reqId={}", session.getSessionId(), reqId);

        SclServer server = getSclServer(session);
        if (server == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String ref = str(req.dataReference);
        if (ref == null) return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        String refAfter = opt(req.refAfterPresent, req.refAfter);

        // Parse and resolve
        RefUtil.RefParts parts = RefUtil.parse(ref);
        if (parts == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        boolean isDoLevel = parts.doName != null;

        SclLN ln;
        List<SclDataDirectoryEntry> allEntries;
        if (isDoLevel) {
            RefUtil.DataResolveResult r = RefUtil.resolveData(server, ref);
            if (r == null || r.doi == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            ln = r.ln;
            allEntries = r.doi.collectDataDirectory(getSclDataTypeTemplates(session), ln);
        } else {
            RefUtil.DataResolveResult r = RefUtil.resolveData(server, parts.ldName + "/" + parts.lnName);
            if (r == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            ln = r.ln;
            allEntries = ln.collectDataDirectory(getSclDataTypeTemplates(session));
        }

        // referenceAfter pagination
        int startIdx = RefUtil.afterIndex(allEntries, refAfter, e -> e.ref);
        if (startIdx < 0) return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        // Build paged response
        CmsGetDataDirectoryResponse resp = new CmsGetDataDirectoryResponse().reqId(reqId);
        int ps = pageSize();
        int count = 0;
        for (int i = startIdx; i < allEntries.size() && count < ps; i++) {
            SclDataDirectoryEntry e = allEntries.get(i);
            CmsSubRefEntry entry = new CmsSubRefEntry().reference(e.ref);
            if (e.fc != null && !e.fc.isEmpty()) entry.fc(CmsFC.fromCode(e.fc));
            resp.dataAttribute.add(entry);
            count++;
        }
        resp.moreFollows(allEntries.size() > startIdx + ps);
        log.info("GetDataDirectory: '{}' -> {} entries (pageSize={})", ref, count, ps);
        return ok(resp, reqId);
    }
}
