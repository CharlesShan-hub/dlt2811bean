package com.ysh.jcms.app.handler.sg.setEditSgValue;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.app.handler.sg.SgSessionState.SgcState;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.sg.CmsSetEditSgValueError;
import com.ysh.jcms.svc.sg.CmsSetEditSgValueRequest;
import com.ysh.jcms.svc.sg.CmsSetEditSgValueResponse;
import com.ysh.jcms.svc.sg.CmsSgRefValueEntry;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class SetEditSgValueServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(SetEditSgValueServer.class);

    public SetEditSgValueServer() {
        super(ServiceName.SET_EDIT_SG_VALUE, CmsSetEditSgValueRequest.class, CmsSetEditSgValueError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsSetEditSgValueRequest req = (CmsSetEditSgValueRequest) decoded;
        // Pre-allocate array elements so the native decoder has valid targets.
        // Without this, C writes decoded count but elements=NULL → InvalidMemoryAccess.
        int n = pageSize();
        req.data.allocSize = n;
        for (int i = 0; i < n; i++) {
            req.data.items.add(new CmsSgRefValueEntry());
        }
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsSetEditSgValueRequest req = (CmsSetEditSgValueRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("SetEditSGValue from {}: reqId={}, {} entries",
            session.getSessionId(), reqId, req.data.count);

        if (req.data == null || req.data.count == 0) {
            // Empty request — success with no changes
            CmsSetEditSgValueResponse resp = new CmsSetEditSgValueResponse()
                .reqId(reqId);
            return buildSuccess(resp.encode(), reqId);
        }

        SgcState state = SgSessionState.getState(session.getSessionId());

        for (int i = 0; i < req.data.count; i++) {
            CmsSgRefValueEntry entry = req.data.items.get(i);
            String ref = entry.reference.len > 0
                ? new String(entry.reference.value(), StandardCharsets.UTF_8) : null;

            if (ref == null || ref.isEmpty()) {
                log.warn("SetEditSGValue: empty reference at index {}", i);
                continue;
            }

            // Encode the CmsData value and store in session state
            byte[] encoded = entry.value.encode();
            state.putEditValue(ref, encoded);
            log.debug("SetEditSGValue: stored ref={} ({} bytes)", ref, encoded.length);
        }

        log.info("SetEditSGValue: stored {} values for session={}",
            req.data.count, session.getSessionId());

        try {
            CmsSetEditSgValueResponse resp = new CmsSetEditSgValueResponse()
                .reqId(reqId);
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode SetEditSGValueResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }
}
