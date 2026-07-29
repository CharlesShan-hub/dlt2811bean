package com.ysh.jcms.app.handler.control.timeActivatedOperate;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.bitarray.CmsCheck;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;
import com.ysh.jcms.pdu.control.CmsTimeActivatedOperateError;
import com.ysh.jcms.pdu.control.CmsTimeActivatedOperateRequest;
import com.ysh.jcms.pdu.control.CmsTimeActivatedOperateResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;
import java.util.Map;

public class TimeActivatedOperateClient extends BaseClientHandler {

    public void execute(String ref, long operTmEpochSeconds, Map<String, String> args) throws Exception {
        CmsTimeActivatedOperateRequest req = new CmsTimeActivatedOperateRequest().reqId(nextReqId()).reference(ref)
                .operTm(new CmsUtcTime().secondsSinceEpoch(operTmEpochSeconds));

        String valueStr = args.get("value");
        if (valueStr != null && !valueStr.isEmpty()) {
            CmsData ctlVal = new CmsData();
            ctlVal.choice.value(CmsData.CHOICE_BOOLEAN);
            ctlVal.alt_boolean.value(Boolean.parseBoolean(valueStr));
            req.ctlVal(ctlVal);
        }

        String originStr = args.get("origin");
        if (originStr != null && !originStr.isEmpty())
            req.origin(new CmsOriginator().orCat(Integer.parseInt(originStr)));

        String ctlNumStr = args.get("ctlNum");
        if (ctlNumStr != null && !ctlNumStr.isEmpty())
            req.ctlNum(Integer.parseInt(ctlNumStr));

        String testStr = args.get("test");
        if (testStr != null)
            req.test(Boolean.parseBoolean(testStr));

        String checkStr = args.get("check");
        if (checkStr != null && !checkStr.isEmpty()) {
            CmsCheck check = new CmsCheck();
            for (String flag : checkStr.split(",")) {
                if ("syncheck".equalsIgnoreCase(flag.trim()))
                    check.syncheck(true);
                if ("interlock".equalsIgnoreCase(flag.trim()))
                    check.interlock_check(true);
            }
            req.check(check);
        }

        send(ServiceName.TIME_ACTIVATED_OPERATE, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsTimeActivatedOperateError());
        throw new IOException("TimeActivatedOperate rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsTimeActivatedOperateResponse());
        log.info("TimeActivatedOperate scheduled");
    }
}
