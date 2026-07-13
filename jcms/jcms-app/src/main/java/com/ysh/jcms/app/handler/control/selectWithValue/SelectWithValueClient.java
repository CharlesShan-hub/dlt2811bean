package com.ysh.jcms.app.handler.control.selectWithValue;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.control.CmsCheck;
import com.ysh.jcms.data.control.CmsOriginator;
import com.ysh.jcms.svc.control.CmsSelectWithValueError;
import com.ysh.jcms.svc.control.CmsSelectWithValueRequest;
import com.ysh.jcms.svc.control.CmsSelectWithValueResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;
import java.util.Map;

public class SelectWithValueClient extends BaseClientHandler {

    public void execute(String ref, Map<String, String> args) throws Exception {
        CmsSelectWithValueRequest req = new CmsSelectWithValueRequest().reqId(nextReqId()).reference(ref);

        String valueStr = args.get("value");
        if (valueStr != null && !valueStr.isEmpty()) {
            CmsData ctlVal = new CmsData();
            ctlVal.choice.value(CmsData.CHOICE_BOOLEAN);
            ctlVal.alt_boolean.value(Boolean.parseBoolean(valueStr));
            req.ctlVal(ctlVal);
        }

        String originStr = args.get("origin");
        if (originStr != null && !originStr.isEmpty()) {
            req.origin(new CmsOriginator().orCat(Integer.parseInt(originStr)));
        }

        String ctlNumStr = args.get("ctlNum");
        if (ctlNumStr != null && !ctlNumStr.isEmpty()) {
            req.ctlNum(Integer.parseInt(ctlNumStr));
        }

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

        send(ServiceName.SELECT_WITH_VALUE, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsSelectWithValueError());
        throw new IOException("SelectWithValue rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsSelectWithValueResponse());
        log.info("SelectWithValue succeeded");
    }
}
