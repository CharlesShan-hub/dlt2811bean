package com.ysh.jcms.app.handler.sg.selectEditSg;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.sg.CmsSelectEditSgError;
import com.ysh.jcms.svc.sg.CmsSelectEditSgRequest;
import com.ysh.jcms.svc.sg.CmsSelectEditSgResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectEditSgServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(SelectEditSgServer.class);

    public SelectEditSgServer() {
        super(ServiceName.SELECT_EDIT_SG, CmsSelectEditSgRequest.class, CmsSelectEditSgError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsSelectEditSgRequest req = (CmsSelectEditSgRequest) rawReq;
        String ref = str(req.sgcbReference);
        int sgNum = req.settingGroupNumber.value() & 0xFF;

        log.info("SelectEditSG from {}: reqId={}, sgcbRef={}, sgNum={}", session.getSessionId(), reqId, ref, sgNum);

        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        int numOfSG = CmsConfigLoader.load().getProtocol().getSetting().getNumOfSG();
        if (sgNum < 1 || sgNum > numOfSG) {
            log.warn("SelectEditSG: invalid group number {} (max={})", sgNum, numOfSG);
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        SgSessionState.getState(session.getSessionId()).setEditSG(sgNum);
        log.info("SelectEditSG: set editSG={} for session={}", sgNum, session.getSessionId());
        return ok(new CmsSelectEditSgResponse().reqId(reqId), reqId);
    }
}
