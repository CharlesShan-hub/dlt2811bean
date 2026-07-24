package com.ysh.jcms.app.handler.sg.selectActiveSg;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.sg.CmsSelectActiveSgError;
import com.ysh.jcms.svc.sg.CmsSelectActiveSgRequest;
import com.ysh.jcms.svc.sg.CmsSelectActiveSgResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectActiveSgServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(SelectActiveSgServer.class);

    public SelectActiveSgServer() {
        super(ServiceName.SELECT_ACTIVE_SG, CmsSelectActiveSgRequest.class, CmsSelectActiveSgError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsSelectActiveSgRequest req = (CmsSelectActiveSgRequest) rawReq;
        String ref = str(req.sgcbReference);
        int sgNum = req.settingGroupNumber.value() & 0xFF;

        log.info("SelectActiveSG from {}: reqId={}, sgcbRef={}, sgNum={}", session.getSessionId(), reqId, ref, sgNum);

        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        int numOfSG = CmsConfigLoader.load().getProtocol().getSetting().getNumOfSG();
        if (sgNum < 1 || sgNum > numOfSG) {
            log.warn("SelectActiveSG: invalid group number {} (max={})", sgNum, numOfSG);
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        SgSessionState.getState(session.getSessionId()).setActSG(sgNum);
        log.info("SelectActiveSG: set actSG={} for session={}", sgNum, session.getSessionId());
        return ok(new CmsSelectActiveSgResponse().reqId(reqId), reqId);
    }
}
