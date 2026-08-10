package com.ysh.jcms.app.handler.data.getDataValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.pdu.data.CmsGetDataValuesError;
import com.ysh.jcms.pdu.data.CmsGetDataValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetDataValuesClient extends BaseClientHandler<GetDataValuesDao> {

    public static final class DataValue {
        public final int choiceType;
        public final String valueString;

        public DataValue(int choiceType, String valueString) {
            this.choiceType = choiceType;
            this.valueString = valueString;
        }
    }

    @Override
    public void execute(GetDataValuesDao dao) throws Exception {
        send(ServiceName.GET_DATA_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataValuesError err = decodeErr(frame, new CmsGetDataValuesError());
        throw new IOException("GetDataValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetDataValuesDao dao) throws IOException {
        PaginationContext ctx = dao.paginationContext();
        CmsGetDataValuesResponse resp = decodeResp(frame, new CmsGetDataValuesResponse());
        log.warn("GetDataValues page: {} values", resp);

        List<DataValue> values = new ArrayList<>();
        for (CmsData d : resp.value) {
            values.add(new DataValue(d.choice(), d.toValueString()));
        }
        ctx.setResult(values);
    }

}
