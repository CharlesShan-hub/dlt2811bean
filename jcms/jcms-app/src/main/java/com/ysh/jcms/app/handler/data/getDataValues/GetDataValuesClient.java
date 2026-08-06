package com.ysh.jcms.app.handler.data.getDataValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.sequence.data.CmsDataRefEntry;
import com.ysh.jcms.pdu.data.CmsGetDataValuesError;
import com.ysh.jcms.pdu.data.CmsGetDataValuesRequest;
import com.ysh.jcms.pdu.data.CmsGetDataValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetDataValuesClient extends BaseClientHandler {

    public static final class DataValue {
        public final int choiceType;
        public final String valueString;

        public DataValue(int choiceType, String valueString) {
            this.choiceType = choiceType;
            this.valueString = valueString;
        }
    }

    private List<DataValue> lastValues = new ArrayList<>();

    public List<DataValue> getLastValues() {
        return lastValues;
    }

    public void execute(GetDataValuesDao dao) throws Exception {
        CmsGetDataValuesRequest req = new CmsGetDataValuesRequest();

        for (GetDataValuesDao.DataRef ref : dao.dataRefs()) {
            CmsDataRefEntry entry = new CmsDataRefEntry().reference(ref.reference());
            if (ref.fc() != null) {
                entry.fc(ref.fc());
            }
            req.data.add(entry);
        }

        send(ServiceName.GET_DATA_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataValuesError err = decodeErr(frame, new CmsGetDataValuesError());
        throw new IOException("GetDataValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetDataValuesResponse resp = decodeResp(frame, new CmsGetDataValuesResponse());

        List<DataValue> values = new ArrayList<>();
        for (CmsData d : resp.value) {
            values.add(new DataValue(d.choice(), d.toValueString()));
        }
        this.lastValues = values;
        log.info("GetDataValues succeeded: {} values", values.size());
    }

}
