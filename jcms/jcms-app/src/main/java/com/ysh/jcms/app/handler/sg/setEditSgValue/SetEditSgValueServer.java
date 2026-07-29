package com.ysh.jcms.app.handler.sg.setEditSgValue;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.app.handler.sg.SgSessionState.SgcState;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.pdu.sg.CmsSetEditSgValueError;
import com.ysh.jcms.pdu.sg.CmsSetEditSgValueRequest;
import com.ysh.jcms.pdu.sg.CmsSetEditSgValueResponse;
import com.ysh.jcms.pdu.sg.CmsSgRefValueEntry;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetEditSgValueServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(SetEditSgValueServer.class);

    public SetEditSgValueServer() {
        super(ServiceName.SET_EDIT_SG_VALUE, CmsSetEditSgValueRequest.class, CmsSetEditSgValueError.class);
    }

    @Override
    protected void prepareDecode(CmsTypeOld decoded) {
        CmsSetEditSgValueRequest req = (CmsSetEditSgValueRequest) decoded;
        int n = pageSize();
        for (int i = 0; i < n; i++) {
            req.data.items.add(new CmsSgRefValueEntry());
        }
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsSetEditSgValueRequest req = (CmsSetEditSgValueRequest) rawReq;
        log.info("SetEditSGValue from {}: reqId={}, {} entries", session.getSessionId(), reqId, req.data.count);

        if (req.data == null || req.data.count == 0)
            return ok(new CmsSetEditSgValueResponse().reqId(reqId), reqId);

        SgcState state = SgSessionState.getState(session.getSessionId());
        for (int i = 0; i < req.data.count; i++) {
            CmsSgRefValueEntry entry = req.data.items.get(i);
            String ref = str(entry.reference);
            if (ref == null) {
                log.warn("SetEditSGValue: empty reference at index {}", i);
                continue;
            }
            state.putEditValue(ref, entry.value.encode());
        }
        log.info("SetEditSGValue: stored {} values for session={}", req.data.count, session.getSessionId());
        return ok(new CmsSetEditSgValueResponse().reqId(reqId), reqId);
    }
}
