package com.ysh.jcms.app.handler.report.setUrcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.report.CmsSetUrcbValuesError;
import com.ysh.jcms.svc.report.CmsSetUrcbValuesRequest;
import com.ysh.jcms.svc.report.CmsSetUrcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetUrcbValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(SetUrcbValuesServer.class);

    public SetUrcbValuesServer() {
        super(ServiceName.SET_URCB_VALUES, CmsSetUrcbValuesRequest.class, CmsSetUrcbValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsSetUrcbValuesRequest req = (CmsSetUrcbValuesRequest) decoded;
        req.urcb.allocSize = pageSize();
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsSetUrcbValuesRequest req = (CmsSetUrcbValuesRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("SetURCBValues from {}: reqId={}, {} entries", session.getSessionId(), reqId, req.urcb.count);

        try {
            CmsSetUrcbValuesResponse resp = new CmsSetUrcbValuesResponse().reqId(reqId);
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode SetURCBValuesResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }
}
