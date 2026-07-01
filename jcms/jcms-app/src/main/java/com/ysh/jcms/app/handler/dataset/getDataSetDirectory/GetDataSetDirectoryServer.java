package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.svc.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.svc.dataset.CmsGetDataSetDirectoryError;
import com.ysh.jcms.svc.dataset.CmsGetDataSetDirectoryRequest;
import com.ysh.jcms.svc.dataset.CmsGetDataSetDirectoryResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.util.RefUtil;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetDataSetDirectoryServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetDataSetDirectoryServer.class);

    public GetDataSetDirectoryServer() {
        super(ServiceName.GET_DATA_SET_DIRECTORY, CmsGetDataSetDirectoryRequest.class, CmsGetDataSetDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetDataSetDirectoryRequest req = (CmsGetDataSetDirectoryRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("GetDataSetDirectory from {}: reqId={}", session.getSessionId(), reqId);

        SclServer server = getSclServer(session);
        if (server == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String ref = str(req.datasetReference);
        if (ref == null) return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        RefUtil.ResolveResult r = RefUtil.resolve(server, ref);
        if (r == null || r.ref.doName == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        SclDataSet dataSet = r.ln.findDataSetByName(r.ref.doName);
        if (dataSet == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String refAfter = opt(req.refAfterPresent, req.refAfter);

        CmsGetDataSetDirectoryResponse resp = new CmsGetDataSetDirectoryResponse().reqId(reqId);
        resp.memberData.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();
        int ps = pageSize(), count = 0;

        for (SclFCDA fcda : dataSet.getFcDas()) {
            if (refAfter != null) {
                if (fcda.buildFcdaRef().equals(refAfter)) { refAfter = null; }
                continue;
            }
            resp.memberData.add(new CmsDataRefFcEntry()
                .reference(fcda.buildFcdaRef())
                .fc(fcda.getFc() != null ? CmsFC.fromCode(fcda.getFc()) : 0));
            if (++count >= ps) break;
        }
        resp.moreFollows(count >= ps);
        log.info("GetDataSetDirectory: '{}' -> {} members", ref, count);
        return ok(resp, reqId);
    }
}
