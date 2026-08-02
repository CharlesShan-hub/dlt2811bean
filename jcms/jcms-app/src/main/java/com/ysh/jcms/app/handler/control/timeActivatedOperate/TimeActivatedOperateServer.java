package com.ysh.jcms.app.handler.control.timeActivatedOperate;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.control.CmsTimeActivatedOperateError;
import com.ysh.jcms.pdu.control.CmsTimeActivatedOperateRequest;
import com.ysh.jcms.pdu.control.CmsTimeActivatedOperateResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeActivatedOperateServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(TimeActivatedOperateServer.class);

    public TimeActivatedOperateServer() {
        super(ServiceName.TIME_ACTIVATED_OPERATE, CmsTimeActivatedOperateRequest.class, CmsTimeActivatedOperateError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsTimeActivatedOperateRequest req = (CmsTimeActivatedOperateRequest) rawReq;
        String ref = str(req.reference);
        log.info("TimeActivatedOperate from {}: reqId={}, ref={}, operTm={}", session.getSessionId(), reqId, ref,
                req.operTm.secondsSinceEpoch.value());

        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        // 模拟实现：记录操作计划，在指定时间执行（当前简化：直接返回成功）
        log.info("TimeActivatedOperate scheduled for ref={}", ref);
        return ok(new CmsTimeActivatedOperateResponse().reference(ref), reqId);
    }
}
