package com.ysh.jcms.app.handler.dataset.getDataSetValues;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.support.PaginationContext;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.pdu.dataset.CmsGetDataSetValuesError;
import com.ysh.jcms.core.pdu.dataset.CmsGetDataSetValuesResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GetDataSetValuesClient extends BaseClientHandler<GetDataSetValuesDao> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void execute(GetDataSetValuesDao dao) throws Exception {
        send(CmsServiceInfo.GET_DATA_SET_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataSetValuesError err = CmsFrameDecoder.decodeErr(frame, new CmsGetDataSetValuesError());
        throw new IOException("GetDataSetValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetDataSetValuesDao dao) throws IOException {
        CmsGetDataSetValuesResponse resp = CmsFrameDecoder.decodeResp(frame, new CmsGetDataSetValuesResponse());
        if (content() != null) {
            // Convert each value to structured JSON
            List<Object> structuredValues = new ArrayList<>();
            for (CmsData data : resp.value) {
                String jsonStr = data.toValueString();
                if (jsonStr.startsWith("{")) {
                    try {
                        structuredValues.add(MAPPER.readValue(jsonStr, Object.class));
                    } catch (Exception e) {
                        structuredValues.add(jsonStr);
                    }
                } else {
                    structuredValues.add(jsonStr);
                }
            }

            boolean moreFollows = resp.moreFollows.value();

            // Accumulate pages
            @SuppressWarnings("unchecked")
            Map<String, Object> resMap = (Map<String, Object>) content().res();
            if (resMap == null) {
                Map<String, Object> newMap = new LinkedHashMap<>();
                newMap.put("value", new ArrayList<>(structuredValues));
                newMap.put("moreFollows", moreFollows ? 1 : 0);
                content().res(newMap);
            } else {
                @SuppressWarnings("unchecked")
                List<Object> existing = (List<Object>) resMap.get("value");
                existing.addAll(structuredValues);
                resMap.put("moreFollows", moreFollows ? 1 : 0);
            }

            // Update pagination context
            PaginationContext ctx = content().paginationContext();
            ctx.setLastMoreFollows(moreFollows);
            if (!structuredValues.isEmpty()) {
                Object lastItem = structuredValues.get(structuredValues.size() - 1);
                ctx.setLastReference(lastItem != null ? lastItem.toString() : null);
            }
        }
    }

    @Override
    protected void setPaginationCursor(GetDataSetValuesDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
