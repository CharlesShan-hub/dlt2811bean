package com.ysh.jcms.app.handler.log.getLogStatusValues;

import com.ysh.jcms.app.handler.base.BaseServerHandler;
import com.ysh.jcms.core.data.choice.CmsLogStatusValueChoice;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.sequence.log.CmsLogStatusValue;
import com.ysh.jcms.core.pdu.log.CmsGetLogStatusValuesError;
import com.ysh.jcms.core.pdu.log.CmsGetLogStatusValuesRequest;
import com.ysh.jcms.core.pdu.log.CmsGetLogStatusValuesResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.log.LogStorage;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

/**
 * GetLogStatusValues — 8.8.6 读日志状态值服务。
 */
public class GetLogStatusValuesServer extends BaseServerHandler<CmsGetLogStatusValuesRequest, CmsGetLogStatusValuesError> {

    private final LogStorage logStorage;

    public GetLogStatusValuesServer() {
        super(CmsServiceInfo.GET_LOG_STATUS_VALUES, CmsGetLogStatusValuesRequest.class, CmsGetLogStatusValuesError.class);
        this.logStorage = new LogStorage(CmsConfigLoader.load().protocol().log().rootPath());
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetLogStatusValuesRequest req, int reqId) {
        log.info("GetLogStatusValues from {}: reqId={}, {} refs", session.sessionId(), reqId, req.logReference.size());

        CmsGetLogStatusValuesResponse resp = new CmsGetLogStatusValuesResponse();

        for (int i = 0; i < req.logReference.size(); i++) {
            String logRef = str(req.logReference.get(i));
            LogStorage.LogStatus status = logStorage.getStatus(logRef);

            CmsLogStatusValueChoice choice = new CmsLogStatusValueChoice();
            if (status == LogStorage.LogStatus.EMPTY || (status.newestTimeMsOfDay == 0 && status.newestTimeDays == 0)) {
                // 无日志，返回 ServiceError=0（表示无状态）
                choice.altError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            } else {
                CmsLogStatusValue val = new CmsLogStatusValue();
                val.oldEntrTm.msOfDay.value(status.oldestTimeMsOfDay);
                val.oldEntrTm.daysSince1984.value(status.oldestTimeDays);
                val.oldEntr.value(status.oldestEntryId);
                val.newEntrTm.msOfDay.value(status.newestTimeMsOfDay);
                val.newEntrTm.daysSince1984.value(status.newestTimeDays);
                val.newEntr.value(status.newestEntryId);
                choice.altValue(val);
            }
            resp.log.add(choice);
        }
        resp.moreFollows(false);

        log.info("GetLogStatusValues: returning {} entries", resp.log.size());
        return ok(resp, reqId);
    }
}
