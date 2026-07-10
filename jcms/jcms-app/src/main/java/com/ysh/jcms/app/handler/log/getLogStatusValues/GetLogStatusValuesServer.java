package com.ysh.jcms.app.handler.log.getLogStatusValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.log.CmsGetLogStatusValuesError;
import com.ysh.jcms.svc.log.CmsGetLogStatusValuesRequest;
import com.ysh.jcms.svc.log.CmsGetLogStatusValuesResponse;
import com.ysh.jcms.svc.log.CmsLogStatusValue;
import com.ysh.jcms.svc.log.CmsLogStatusValueChoice;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.log.LogStorage;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GetLogStatusValues — 8.8.6 读日志状态值服务。
 */
public class GetLogStatusValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetLogStatusValuesServer.class);

    private final LogStorage logStorage;

    public GetLogStatusValuesServer() {
        super(ServiceName.GET_LOG_STATUS_VALUES, CmsGetLogStatusValuesRequest.class, CmsGetLogStatusValuesError.class);
        this.logStorage = new LogStorage(CmsConfigLoader.load().getProtocol().getLog().getRootPath());
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsGetLogStatusValuesRequest req = (CmsGetLogStatusValuesRequest) decoded;
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetLogStatusValuesRequest req = (CmsGetLogStatusValuesRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("GetLogStatusValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.logReference.count);

        CmsGetLogStatusValuesResponse resp = new CmsGetLogStatusValuesResponse().reqId(reqId);

        for (int i = 0; i < req.logReference.count; i++) {
            String logRef = str(req.logReference.items.get(i).value());
            LogStorage.LogStatus status = logStorage.getStatus(logRef);

            CmsLogStatusValueChoice choice = new CmsLogStatusValueChoice();
            if (status == LogStorage.LogStatus.EMPTY || (status.newestTimeMsOfDay == 0 && status.newestTimeDays == 0)) {
                // 无日志，返回 ServiceError=0（表示无状态）
                choice.choice(CmsLogStatusValueChoice.ERROR);
                choice.altError.value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            } else {
                choice.choice(CmsLogStatusValueChoice.VALUE);
                CmsLogStatusValue val = new CmsLogStatusValue();
                val.oldEntrTm.msOfDay.value(status.oldestTimeMsOfDay);
                val.oldEntrTm.daysSince1984.value(status.oldestTimeDays);
                val.oldEntr.value(status.oldestEntryId);
                val.newEntrTm.msOfDay.value(status.newestTimeMsOfDay);
                val.newEntrTm.daysSince1984.value(status.newestTimeDays);
                val.newEntr.value(status.newestEntryId);
                choice.altValue = val;
            }
            resp.log.add(choice);
        }
        resp.moreFollows(false);

        log.info("GetLogStatusValues: returning {} entries", resp.log.items.size());
        return ok(resp, reqId);
    }
}
