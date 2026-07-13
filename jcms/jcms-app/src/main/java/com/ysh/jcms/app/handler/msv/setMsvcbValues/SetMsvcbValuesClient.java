package com.ysh.jcms.app.handler.msv.setMsvcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.svc.msv.CmsSetMsvcbEntry;
import com.ysh.jcms.svc.msv.CmsSetMsvcbValuesError;
import com.ysh.jcms.svc.msv.CmsSetMsvcbValuesRequest;
import com.ysh.jcms.svc.msv.CmsSetMsvcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class SetMsvcbValuesClient extends BaseClientHandler {

    public void execute(String ref, String svEna, String msvId, String datSet) throws Exception {
        CmsSetMsvcbEntry entry = new CmsSetMsvcbEntry().reference(ref);
        if (svEna != null && !svEna.isEmpty())
            entry.svEnaPresent(true).svEna(Boolean.parseBoolean(svEna));
        if (msvId != null && !msvId.isEmpty())
            entry.msvId(msvId);
        if (datSet != null && !datSet.isEmpty())
            entry.datSet(datSet);

        CmsSetMsvcbValuesRequest req = new CmsSetMsvcbValuesRequest().reqId(nextReqId());
        req.msvcb.add(entry);
        send(ServiceName.SET_MSVCB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsSetMsvcbValuesError());
        throw new IOException("SetMSVCBValues rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsSetMsvcbValuesResponse());
        log.info("SetMSVCBValues succeeded");
    }
}
