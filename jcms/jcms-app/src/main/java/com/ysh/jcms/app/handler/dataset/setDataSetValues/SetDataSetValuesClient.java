package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.pdu.dataset.CmsSetDataSetValuesError;
import com.ysh.jcms.pdu.dataset.CmsSetDataSetValuesRequest;
import com.ysh.jcms.pdu.dataset.CmsSetDataSetValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetDataSetValuesClient extends BaseClientHandler {

    public void execute(SetDataSetValuesDao dao) throws Exception {
        CmsSetDataSetValuesRequest req = new CmsSetDataSetValuesRequest().reqId(nextReqId()).datasetReference(dao.datasetReference())
                .refAfter(dao.referenceAfter());

        for (String val : dao.values()) {
            CmsData data = new CmsData();
            data.choice(CmsData.CHOICE_VISIBLE_STRING);
            data.alt_visible_string.value(val);
            req.value.add(data);
        }

        send(ServiceName.SET_DATA_SET_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetDataSetValuesError err = decodeErr(frame, new CmsSetDataSetValuesError());
        StringBuilder sb = new StringBuilder("SetDataSetValues partially failed: ");
        for (int i = 0; i < err.result.count; i++) {
            if (i > 0)
                sb.append(", ");
            sb.append("[").append(i).append("]=").append(err.result.items.get(i).value());
        }
        throw new IOException(sb.toString());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetDataSetValuesResponse resp = new CmsSetDataSetValuesResponse();
        resp.decode(frame.asduBytes());
        log.info("SetDataSetValues succeeded");
    }
}
