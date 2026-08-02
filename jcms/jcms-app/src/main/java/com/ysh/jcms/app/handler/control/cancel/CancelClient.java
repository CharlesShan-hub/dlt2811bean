package com.ysh.jcms.app.handler.control.cancel;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.pdu.control.CmsCancelError;
import com.ysh.jcms.pdu.control.CmsCancelRequest;
import com.ysh.jcms.pdu.control.CmsCancelResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;
import java.util.Map;

public class CancelClient extends BaseClientHandler {

    public void execute(String ref, Map<String, String> args) throws Exception {
        CmsCancelRequest req = new CmsCancelRequest().reference(ref);

        String valueStr = args.get("value");
        if (valueStr != null && !valueStr.isEmpty()) {
            CmsData ctlVal = new CmsData();
            ctlVal.alt_boolean(Boolean.parseBoolean(valueStr));
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

        send(ServiceName.CANCEL, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsCancelError());
        throw new IOException("Cancel rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsCancelResponse());
        log.info("Cancel succeeded");
    }
}
