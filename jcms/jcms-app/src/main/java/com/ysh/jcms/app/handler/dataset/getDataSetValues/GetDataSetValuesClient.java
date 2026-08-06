package com.ysh.jcms.app.handler.dataset.getDataSetValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetValuesError;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetValuesRequest;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetDataSetValuesClient extends BaseClientHandler<GetDataSetValuesDao> {

    public static final class DataSetValue {
        public final int choiceType;
        public final String valueString;

        public DataSetValue(int choiceType, String valueString) {
            this.choiceType = choiceType;
            this.valueString = valueString;
        }
    }

    private List<DataSetValue> lastValues = new ArrayList<>();

    public List<DataSetValue> getLastValues() {
        return lastValues;
    }

    @Override
    public void execute(GetDataSetValuesDao dao) throws Exception {
        CmsGetDataSetValuesRequest req = new CmsGetDataSetValuesRequest().datasetReference(dao.datasetReference())
                .referenceAfter(dao.referenceAfter());

        send(ServiceName.GET_DATA_SET_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataSetValuesError err = decodeErr(frame, new CmsGetDataSetValuesError());
        throw new IOException("GetDataSetValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetDataSetValuesResponse resp = decodeResp(frame, new CmsGetDataSetValuesResponse());

        List<DataSetValue> entries = new ArrayList<>();
        for (CmsData src : resp.value) {
            int ct = src.choice();
            if (ct == 0)
                continue;
            String val = src.toValueString();
            entries.add(new DataSetValue(ct, val));
        }
        this.lastValues = entries;
        log.info("GetDataSetValues succeeded: {} values", entries.size());
    }

}
