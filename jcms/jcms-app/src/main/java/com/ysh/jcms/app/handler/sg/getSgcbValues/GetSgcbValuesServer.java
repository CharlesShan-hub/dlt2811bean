package com.ysh.jcms.app.handler.sg.getSgcbValues;

import com.ysh.jcms.app.handler.base.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.app.handler.sg.SgSessionState.SgcState;
import com.ysh.jcms.core.data.choice.CmsSgcbValueChoice;
import com.ysh.jcms.core.data.sequence.block.CmsSgcb;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.pdu.sg.CmsGetSgcbValuesError;
import com.ysh.jcms.core.pdu.sg.CmsGetSgcbValuesRequest;
import com.ysh.jcms.core.pdu.sg.CmsGetSgcbValuesResponse;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.state.CbStateManager;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetSgcbValuesServer extends BaseServerHandler<CmsGetSgcbValuesRequest, CmsGetSgcbValuesError> {

    public GetSgcbValuesServer() {
        super(CmsServiceInfo.GET_SGCB_VALUES, CmsGetSgcbValuesRequest.class, CmsGetSgcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetSgcbValuesRequest req, int reqId) {
        log.info("GetSGCBValues from {}: reqId={}, {} refs", session.sessionId(), reqId, req.sgcbReference.size());

        SclIED ied = requireIed(session, reqId);
        requireAp(session, reqId);

        CmsGetSgcbValuesResponse resp = new CmsGetSgcbValuesResponse();

        CmsConfig.Protocol.Setting setting = CmsConfigLoader.load().protocol().setting();

        for (CmsObjectReference refObj : req.sgcbReference) {
            String ref = str(refObj);
            CmsSgcbValueChoice choice;
            if (!setting.sgDefaultEnabled()) {
                choice = new CmsSgcbValueChoice().altError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            } else if (CbStateManager.SGCB.get(ref) != null) {
                choice = new CmsSgcbValueChoice().altValue(buildSgcb(ref, session, setting.numOfSG()));
            } else {
                log.warn("GetSGCBValues: ref '{}' not found in SGCB cache (only LLN0.SG is valid)", ref);
                choice = new CmsSgcbValueChoice().altError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            resp.sgscb.add(choice);
        }
        resp.moreFollows(false);
        log.info("GetSGCBValues: returning {} entries", resp.sgscb.size());
        return ok(resp, reqId);
    }

    private static CmsSgcb buildSgcb(String ref, Session session, int numOfSG) {
        SgcState state = SgSessionState.getState(session.sessionId());
        CmsSgcb sgcb = new CmsSgcb().numOfSG(numOfSG).actSG(state.getActSG()).editSG(state.getEditSG());
        sgcb.tActEdt.now();
        sgcb.setPresent("resvTms", false);
        return sgcb;
    }
}
