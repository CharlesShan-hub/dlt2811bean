package com.ysh.jcms.app.handler.sg.selectEditSg;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.sg.CmsSelectEditSgError;
import com.ysh.jcms.pdu.sg.CmsSelectEditSgRequest;
import com.ysh.jcms.pdu.sg.CmsSelectEditSgResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class SelectEditSgServer extends BaseServerHandler<CmsSelectEditSgRequest, CmsSelectEditSgError> {

    public SelectEditSgServer() {
        super(ServiceName.SELECT_EDIT_SG, CmsSelectEditSgRequest.class, CmsSelectEditSgError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsSelectEditSgRequest req, int reqId) {
        String ref = str(req.sgcbReference);
        int sgNum = req.settingGroupNumber.value() & 0xFF;

        log.info("SelectEditSG from {}: reqId={}, sgcbRef={}, sgNum={}", session.sessionId(), reqId, ref, sgNum);

        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        int numOfSG = CmsConfigLoader.load().protocol().setting().numOfSG();
        if (sgNum < 1 || sgNum > numOfSG) {
            log.warn("SelectEditSG: invalid group number {} (max={})", sgNum, numOfSG);
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        SgSessionState.getState(session.sessionId()).setEditSG(sgNum);
        log.info("SelectEditSG: set editSG={} for session={}", sgNum, session.sessionId());
        return ok(new CmsSelectEditSgResponse(), reqId);
    }
}
