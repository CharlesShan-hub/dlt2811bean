package com.ysh.jcms.app.handler.msv.setMsvcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.sequence.msv.CmsSetMsvcbEntry;
import com.ysh.jcms.pdu.msv.CmsSetMsvcbValuesError;
import com.ysh.jcms.pdu.msv.CmsSetMsvcbValuesRequest;
import com.ysh.jcms.pdu.msv.CmsSetMsvcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class SetMsvcbValuesClient extends BaseClientHandler<SetMsvcbValuesDao> {

    @Override
    public void execute(SetMsvcbValuesDao dao) throws Exception {
        CmsSetMsvcbEntry entry = new CmsSetMsvcbEntry().reference(dao.ref());
        if (dao.svEna() != null && !dao.svEna().isEmpty())
            entry.svEna(Boolean.parseBoolean(dao.svEna()));
        if (dao.msvId() != null && !dao.msvId().isEmpty())
            entry.msvID(dao.msvId());
        if (dao.datSet() != null && !dao.datSet().isEmpty())
            entry.datSet(dao.datSet());

        CmsSetMsvcbValuesRequest req = new CmsSetMsvcbValuesRequest();
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
