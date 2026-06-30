package com.ysh.jcms.app.handler.sg.selectActiveSg;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.app.handler.sg.SgSessionState.SgcState;
import com.ysh.jcms.core.CmsType;
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

import java.nio.charset.StandardCharsets;

public class SelectActiveSgServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(SelectActiveSgServer.class);

    public SelectActiveSgServer() {
        super(ServiceName.SELECT_ACTIVE_SG, CmsSelectActiveSgRequest.class, CmsSelectActiveSgError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsSelectActiveSgRequest req = (CmsSelectActiveSgRequest) rawReq;
        int reqId = req.reqId.value();

        String ref = req.sgcbReference.len > 0
            ? new String(req.sgcbReference.value(), StandardCharsets.UTF_8) : null;
        int sgNum = req.settingGroupNumber.value() & 0xFF;

        log.info("SelectActiveSG from {}: reqId={}, sgcbRef={}, sgNum={}",
            session.getSessionId(), reqId, ref, sgNum);

        if (ref == null || ref.isEmpty()) {
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int numOfSG = CmsConfigLoader.load().getProtocol().getSetting().getNumOfSG();
        if (sgNum < 1 || sgNum > numOfSG) {
            log.warn("SelectActiveSG: invalid group number {} (max={})", sgNum, numOfSG);
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        SgcState state = SgSessionState.getState(session.getSessionId());
        state.setActSG(sgNum);

        log.info("SelectActiveSG: set actSG={} for session={}", sgNum, session.getSessionId());

        try {
            CmsSelectActiveSgResponse resp = new CmsSelectActiveSgResponse()
                .reqId(reqId);
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode SelectActiveSGResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }
}
