package com.ysh.jcms.app.handler.dataset.getDataSetValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetValuesError;
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

    @Override
    public void execute(GetDataSetValuesDao dao) throws Exception {
        execute(dao, new PaginationContext());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(GetDataSetValuesDao dao, PaginationContext ctx) throws Exception {
        ctx.setResult(new ArrayList<DataSetValue>());
        send(ServiceName.GET_DATA_SET_VALUES, dao, ctx);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataSetValuesError err = decodeErr(frame, new CmsGetDataSetValuesError());
        throw new IOException("GetDataSetValues rejected: " + err.value());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onSuccess(Frame frame, PaginationContext ctx) throws IOException {
        CmsGetDataSetValuesResponse resp = decodeResp(frame, new CmsGetDataSetValuesResponse());

        List<DataSetValue> entries = new ArrayList<>();
        for (CmsData src : resp.value) {
            int ct = src.choice();
            if (ct == 0)
                continue;
            String val = src.toValueString();
            entries.add(new DataSetValue(ct, val));
        }
        List<DataSetValue> all = (List<DataSetValue>) ctx.getResult();
        all.addAll(entries);
        ctx.setLastMoreFollows(resp.moreFollows.value());
        // GetDataSetValuesResponse's value is List<CmsData> (no reference field),
        // so we use the index-based approach: lastReference is not applicable
        // for this response type. Auto-pull will not function for this service.
        log.info("GetDataSetValues page: {} values (moreFollows={})", entries.size(), ctx.isLastMoreFollows());
    }

    @Override
    protected void setPaginationCursor(GetDataSetValuesDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
