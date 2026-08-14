package com.ysh.jcms.app.handler.sg.selectActiveSg;

import com.ysh.jcms.app.handler.base.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.pdu.sg.CmsSelectActiveSgError;
import com.ysh.jcms.core.pdu.sg.CmsSelectActiveSgRequest;
import com.ysh.jcms.core.pdu.sg.CmsSelectActiveSgResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class SelectActiveSgServer extends BaseServerHandler<CmsSelectActiveSgRequest, CmsSelectActiveSgError> {

    public SelectActiveSgServer() {
        super(CmsServiceInfo.SELECT_ACTIVE_SG, CmsSelectActiveSgRequest.class, CmsSelectActiveSgError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsSelectActiveSgRequest req, int reqId) {
        String ref = str(req.sgcbReference);
        int sgNum = req.settingGroupNumber.value() & 0xFF;

        log.info("SelectActiveSG from {}: reqId={}, sgcbRef={}, sgNum={}", session.sessionId(), reqId, ref, sgNum);

        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        int numOfSG = CmsConfigLoader.load().protocol().setting().numOfSG();
        if (sgNum < 1 || sgNum > numOfSG) {
            log.warn("SelectActiveSG: invalid group number {} (max={})", sgNum, numOfSG);
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        SgSessionState.getState(session.sessionId()).setActSG(sgNum);
        log.info("SelectActiveSG: set actSG={} for session={}", sgNum, session.sessionId());
        return ok(new CmsSelectActiveSgResponse(), reqId);
    }
}
