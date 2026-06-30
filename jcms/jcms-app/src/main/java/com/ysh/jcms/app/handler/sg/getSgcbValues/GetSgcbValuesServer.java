package com.ysh.jcms.app.handler.sg.getSgcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsSgcb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.sg.CmsGetSgcbValuesError;
import com.ysh.jcms.svc.sg.CmsGetSgcbValuesRequest;
import com.ysh.jcms.svc.sg.CmsGetSgcbValuesResponse;
import com.ysh.jcms.svc.sg.CmsSgcbValueChoice;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GetSgcbValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetSgcbValuesServer.class);

    /** Per-session SGCB state (actSG, editSG). */
    private static final ConcurrentMap<String, SgcState> SESSION_STATES = new ConcurrentHashMap<>();

    public GetSgcbValuesServer() {
        super(ServiceName.GET_SGCB_VALUES, CmsGetSgcbValuesRequest.class, CmsGetSgcbValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsGetSgcbValuesRequest req = (CmsGetSgcbValuesRequest) decoded;
        req.sgcbReference.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetSgcbValuesRequest req = (CmsGetSgcbValuesRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("GetSGCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.sgcbReference.count);

        CmsGetSgcbValuesResponse resp = new CmsGetSgcbValuesResponse()
            .reqId(reqId);
        resp.sgscb.allocSize = Math.max(req.sgcbReference.count, 1);

        CmsConfig.Protocol.Setting setting = CmsConfigLoader.load().getProtocol().getSetting();
        int numOfSG = setting.getNumOfSG();
        boolean sgEnabled = setting.isSgDefaultEnabled();

        for (int i = 0; i < req.sgcbReference.count; i++) {
            String ref = new String(req.sgcbReference.items.get(i).value(), StandardCharsets.UTF_8);
            log.debug("GetSGCBValues: resolving ref={}", ref);

            CmsSgcbValueChoice choice = new CmsSgcbValueChoice();
            if (!sgEnabled) {
                choice.choice(CmsSgcbValueChoice.ERROR);
                choice.altError.value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            } else {
                choice.choice(CmsSgcbValueChoice.VALUE);
                choice.altValue = buildSgcb(ref, session, numOfSG);
            }
            resp.sgscb.add(choice);
        }

        resp.moreFollows(false);

        log.info("GetSGCBValues: returning {} entries", resp.sgscb.count);

        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode GetSGCBValuesResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private static CmsSgcb buildSgcb(String ref, Session session, int numOfSG) {
        SgcState state = SESSION_STATES.computeIfAbsent(session.getSessionId(), k -> new SgcState());
        CmsSgcb sgcb = new CmsSgcb()
            .numOfSG(numOfSG)
            .actSG(state.actSG)
            .editSG(state.editSG);
        sgcb.tActEdt.now();
        sgcb.resvTms_present(false);
        return sgcb;
    }

    private static class SgcState {
        int actSG = 1;
        int editSG = 1;
    }
}
