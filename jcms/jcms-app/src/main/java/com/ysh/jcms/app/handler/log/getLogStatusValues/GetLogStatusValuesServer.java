package com.ysh.jcms.app.handler.log.getLogStatusValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.log.CmsGetLogStatusValuesError;
import com.ysh.jcms.svc.log.CmsGetLogStatusValuesRequest;
import com.ysh.jcms.svc.log.CmsGetLogStatusValuesResponse;
import com.ysh.jcms.svc.log.CmsLogStatusValue;
import com.ysh.jcms.svc.log.CmsLogStatusValueChoice;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GetLogStatusValues — 8.8.6 读日志状态值服务。
 *
 * <p>当前实现返回空状态（无日志数据）。
 * 实际日志存储和查询需要持久化层配合。
 */
public class GetLogStatusValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetLogStatusValuesServer.class);

    public GetLogStatusValuesServer() {
        super(ServiceName.GET_LOG_STATUS_VALUES, CmsGetLogStatusValuesRequest.class, CmsGetLogStatusValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsGetLogStatusValuesRequest req = (CmsGetLogStatusValuesRequest) decoded;
        req.logReference.allocSize = pageSize();
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetLogStatusValuesRequest req = (CmsGetLogStatusValuesRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("GetLogStatusValues from {}: reqId={}, {} refs",
            session.getSessionId(), reqId, req.logReference.count);

        CmsGetLogStatusValuesResponse resp = new CmsGetLogStatusValuesResponse().reqId(reqId);

        // 返回空状态 — 需要日志持久化层时再实现
        for (int i = 0; i < req.logReference.count; i++) {
            CmsLogStatusValueChoice choice = new CmsLogStatusValueChoice();
            choice.choice(CmsLogStatusValueChoice.VALUE);
            choice.altValue = new CmsLogStatusValue();
            resp.log.add(choice);
        }
        resp.moreFollows(false);

        log.info("GetLogStatusValues: returning {} entries", resp.log.items.size());
        return ok(resp, reqId);
    }
}
