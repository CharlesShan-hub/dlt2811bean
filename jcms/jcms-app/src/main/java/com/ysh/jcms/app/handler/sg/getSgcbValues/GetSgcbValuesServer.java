package com.ysh.jcms.app.handler.sg.getSgcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.app.handler.sg.SgSessionState.SgcState;
import com.ysh.jcms.data.choice.CmsSgcbValueChoice;
import com.ysh.jcms.data.sequence.block.CmsSgcb;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.sg.CmsGetSgcbValuesError;
import com.ysh.jcms.pdu.sg.CmsGetSgcbValuesRequest;
import com.ysh.jcms.pdu.sg.CmsGetSgcbValuesResponse;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetSgcbValuesServer extends BaseServerHandler<CmsGetSgcbValuesRequest, CmsGetSgcbValuesError> {

    public GetSgcbValuesServer() {
        super(ServiceName.GET_SGCB_VALUES, CmsGetSgcbValuesRequest.class, CmsGetSgcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetSgcbValuesRequest req, int reqId) {
        log.info("GetSGCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.sgcbReference.size());

        CmsGetSgcbValuesResponse resp = new CmsGetSgcbValuesResponse();

        CmsConfig.Protocol.Setting setting = CmsConfigLoader.load().getProtocol().getSetting();
        int numOfSG = setting.getNumOfSG();
        boolean sgEnabled = setting.isSgDefaultEnabled();

        for (CmsObjectReference refObj : req.sgcbReference) {
            String ref = str(refObj);
            CmsSgcbValueChoice choice;
            if (!sgEnabled) {
                choice = new CmsSgcbValueChoice().altError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            } else {
                choice = new CmsSgcbValueChoice().altValue(buildSgcb(ref, session, numOfSG));
            }
            resp.sgscb.add(choice);
        }
        resp.moreFollows(false);
        log.info("GetSGCBValues: returning {} entries", resp.sgscb.size());
        return ok(resp, reqId);
    }

    private static CmsSgcb buildSgcb(String ref, Session session, int numOfSG) {
        SgcState state = SgSessionState.getState(session.getSessionId());
        CmsSgcb sgcb = new CmsSgcb().numOfSG(numOfSG).actSG(state.getActSG()).editSG(state.getEditSG());
        sgcb.tActEdt.now();
        sgcb.setPresent("resvTms", false);
        return sgcb;
    }
}
