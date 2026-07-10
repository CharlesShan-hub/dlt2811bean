package com.ysh.jcms.app.handler.sg.getSgcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.app.handler.sg.SgSessionState.SgcState;
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

public class GetSgcbValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetSgcbValuesServer.class);

    public GetSgcbValuesServer() {
        super(ServiceName.GET_SGCB_VALUES, CmsGetSgcbValuesRequest.class, CmsGetSgcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsGetSgcbValuesRequest req = (CmsGetSgcbValuesRequest) rawReq;
        log.info("GetSGCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.sgcbReference.count);

        CmsGetSgcbValuesResponse resp = new CmsGetSgcbValuesResponse().reqId(reqId);

        CmsConfig.Protocol.Setting setting = CmsConfigLoader.load().getProtocol().getSetting();
        int numOfSG = setting.getNumOfSG();
        boolean sgEnabled = setting.isSgDefaultEnabled();

        for (int i = 0; i < req.sgcbReference.count; i++) {
            String ref = str(req.sgcbReference.items.get(i));
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
        return ok(resp, reqId);
    }

    private static CmsSgcb buildSgcb(String ref, Session session, int numOfSG) {
        SgcState state = SgSessionState.getState(session.getSessionId());
        CmsSgcb sgcb = new CmsSgcb().numOfSG(numOfSG).actSG(state.getActSG()).editSG(state.getEditSG());
        sgcb.tActEdt.now();
        sgcb.resvTms_present(false);
        return sgcb;
    }
}
