package com.ysh.jcms.app.handler.control.operate;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.bitarray.CmsCheck;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.pdu.control.CmsOperateError;
import com.ysh.jcms.pdu.control.CmsOperateRequest;
import com.ysh.jcms.pdu.control.CmsOperateResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;
import java.util.Map;

public class OperateClient extends BaseClientHandler<OperateDao> {

    @Override
    public void execute(OperateDao dao) throws Exception {
        CmsOperateRequest req = new CmsOperateRequest().reference(dao.ref());

        Map<String, String> args = dao.args();
        // ctlVal — optional, from --value flag (boolean for SPC)
        String valueStr = args.get("value");
        if (valueStr != null && !valueStr.isEmpty()) {
            CmsData ctlVal = new CmsData();
            ctlVal.alt_boolean(Boolean.parseBoolean(valueStr));
            req.ctlVal(ctlVal);
        }

        // origin — optional, defaults to 0
        String originStr = args.get("origin");
        if (originStr != null && !originStr.isEmpty()) {
            CmsOriginator origin = new CmsOriginator().orCat(Integer.parseInt(originStr));
            req.origin(origin);
        }

        // ctlNum — optional
        String ctlNumStr = args.get("ctlNum");
        if (ctlNumStr != null && !ctlNumStr.isEmpty()) {
            req.ctlNum(Integer.parseInt(ctlNumStr));
        }

        // test — optional, defaults to false
        String testStr = args.get("test");
        if (testStr != null)
            req.test(Boolean.parseBoolean(testStr));

        // check — optional, --check syncheck/interlock-check (comma separated)
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

        send(ServiceName.OPERATE, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsOperateError());
        throw new IOException("Operate rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsOperateResponse());
        log.info("Operate succeeded");
    }
}
