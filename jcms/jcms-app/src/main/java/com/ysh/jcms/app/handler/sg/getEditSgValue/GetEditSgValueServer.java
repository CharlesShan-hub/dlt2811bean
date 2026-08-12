package com.ysh.jcms.app.handler.sg.getEditSgValue;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.app.handler.sg.SgSessionState.SgcState;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.sequence.sg.CmsSgRefFcEntry;
import com.ysh.jcms.core.info.CmsFCInfo;
import com.ysh.jcms.core.pdu.sg.CmsGetEditSgValueError;
import com.ysh.jcms.core.pdu.sg.CmsGetEditSgValueRequest;
import com.ysh.jcms.core.pdu.sg.CmsGetEditSgValueResponse;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.convert.DataConverter;
import com.ysh.jcms.utils.scl.convert.DataValueResolver;
import com.ysh.jcms.utils.scl.convert.DataValueEntry;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetEditSgValueServer extends BaseServerHandler<CmsGetEditSgValueRequest, CmsGetEditSgValueError> {

    public GetEditSgValueServer() {
        super(ServiceName.GET_EDIT_SG_VALUE, CmsGetEditSgValueRequest.class, CmsGetEditSgValueError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetEditSgValueRequest req, int reqId) {
        log.info("GetEditSGValue from {}: reqId={}, {} refs", session.sessionId(), reqId, req.data.size());

        SgcState state = SgSessionState.getState(session.sessionId());
        SclDocument doc = requireScl(session, reqId);

        CmsGetEditSgValueResponse resp = new CmsGetEditSgValueResponse();

        for (int i = 0; i < req.data.size(); i++) {
            CmsSgRefFcEntry entry = req.data.get(i);
            String ref = str(entry.reference);
            if (ref == null)
                continue;

            int fcVal = entry.fc.value();
            boolean isSE = fcVal >= 0 && fcVal < CmsFCInfo.values().length && "SE".equals(CmsFCInfo.values()[fcVal].name());

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

            DataValueEntry dv = DataValueResolver.resolve(doc, ref);
            if (dv != null && dv.val() != null && !dv.val().isEmpty()) {
                resp.value.add(DataConverter.toCmsData(dv));
            } else {
                resp.value.add(new CmsData().alt_visible_string("(unavailable)"));
            }
        }
        resp.moreFollows(false);
        log.info("GetEditSGValue: returning {} values", resp.value.size());
        return ok(resp, reqId);
    }
}
