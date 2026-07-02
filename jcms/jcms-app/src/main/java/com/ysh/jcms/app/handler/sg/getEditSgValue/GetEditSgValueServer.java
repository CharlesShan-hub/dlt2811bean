package com.ysh.jcms.app.handler.sg.getEditSgValue;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.app.handler.sg.SgSessionState.SgcState;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.info.FunctionalConstraint;
import com.ysh.jcms.svc.sg.CmsGetEditSgValueError;
import com.ysh.jcms.svc.sg.CmsGetEditSgValueRequest;
import com.ysh.jcms.svc.sg.CmsGetEditSgValueResponse;
import com.ysh.jcms.svc.sg.CmsSgRefFcEntry;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.data.SclDataValue;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.util.SclDataConverter;
import com.ysh.jcms.utils.scl.util.SclDataValueResolver;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetEditSgValueServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetEditSgValueServer.class);

    public GetEditSgValueServer() {
        super(ServiceName.GET_EDIT_SG_VALUE, CmsGetEditSgValueRequest.class, CmsGetEditSgValueError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetEditSgValueRequest req = (CmsGetEditSgValueRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("GetEditSGValue from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.data.count);

        SgcState state = SgSessionState.getState(session.getSessionId());
        SclServer server = getSclServer(session);
        SclDataTypeTemplates templates = getSclDataTypeTemplates(session);

        CmsGetEditSgValueResponse resp = new CmsGetEditSgValueResponse().reqId(reqId);

        for (int i = 0; i < req.data.count; i++) {
            CmsSgRefFcEntry entry = req.data.items.get(i);
            String ref = str(entry.reference);
            if (ref == null) continue;

            int fcVal = entry.fc.value();
            boolean isSE = fcVal >= 0 && fcVal < FunctionalConstraint.values().length
                && "SE".equals(FunctionalConstraint.values()[fcVal].name());

            byte[] val = isSE ? state.getEditValue(ref) : state.getCommittedValue(ref);
            if (val != null) {
                try {
                    CmsData data = new CmsData();
                    data.decode(val);
                    resp.value.add(data);
                    continue;
                } catch (Exception e) {
                    log.warn("GetEditSGValue: decode failed for ref={}", ref, e);
                }
            }

            if (server == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            SclDataValue sv = SclDataValueResolver.resolveDataValue(server, ref, templates, null);
            if (sv != null && sv.val != null && !sv.val.isEmpty()) {
                resp.value.add(SclDataConverter.toCmsData(sv));
            } else {
                CmsData err = new CmsData();
                err.choice(CmsData.CHOICE_VISIBLE_STRING);
                err.alt_visible_string.value("(unavailable)");
                resp.value.add(err);
            }
        }
        resp.moreFollows(false);
        log.info("GetEditSGValue: returning {} values", resp.value.count);
        return ok(resp, reqId);
    }
}
