package com.ysh.jcms.app.handler.sg.setEditSgValue;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.app.handler.sg.SgSessionState.SgcState;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.sg.CmsSgRefValueEntry;
import com.ysh.jcms.pdu.sg.CmsSetEditSgValueError;
import com.ysh.jcms.pdu.sg.CmsSetEditSgValueRequest;
import com.ysh.jcms.pdu.sg.CmsSetEditSgValueResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class SetEditSgValueServer extends BaseServerHandler<CmsSetEditSgValueRequest, CmsSetEditSgValueError> {

    public SetEditSgValueServer() {
        super(ServiceName.SET_EDIT_SG_VALUE, CmsSetEditSgValueRequest.class, CmsSetEditSgValueError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsSetEditSgValueRequest req = (CmsSetEditSgValueRequest) decoded;
        int n = pageSize();
        for (int i = 0; i < n; i++) {
            req.data.add(new CmsSgRefValueEntry());
        }
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsSetEditSgValueRequest req, int reqId) {
        log.info("SetEditSGValue from {}: reqId={}, {} entries", session.getSessionId(), reqId, req.data.size());

        if (req.data == null || req.data.size() == 0)
            return ok(new CmsSetEditSgValueResponse(), reqId);

        SgcState state = SgSessionState.getState(session.getSessionId());
        for (int i = 0; i < req.data.size(); i++) {
            CmsSgRefValueEntry entry = req.data.get(i);
            String ref = str(entry.reference);
            if (ref == null) {
                log.warn("SetEditSGValue: empty reference at index {}", i);
                continue;
            }
            state.putEditValue(ref, entry.value.encode());
        }
        log.info("SetEditSGValue: stored {} values for session={}", req.data.size(), session.getSessionId());
        return ok(new CmsSetEditSgValueResponse(), reqId);
    }
}
