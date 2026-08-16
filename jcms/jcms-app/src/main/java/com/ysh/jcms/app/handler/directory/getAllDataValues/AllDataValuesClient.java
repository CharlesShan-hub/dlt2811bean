package com.ysh.jcms.app.handler.directory.getAllDataValues;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.support.PaginationContext;
import com.ysh.jcms.core.data.sequence.directory.CmsDataValueEntry;
import com.ysh.jcms.core.pdu.directory.CmsGetAllDataValuesError;
import com.ysh.jcms.core.pdu.directory.CmsGetAllDataValuesResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AllDataValuesClient extends BaseClientHandler<AllDataValuesDao> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void execute(AllDataValuesDao dao) throws Exception {
        send(CmsServiceInfo.GET_ALL_DATA_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllDataValuesError err = CmsFrameDecoder.decodeErr(frame, new CmsGetAllDataValuesError());
        throw new IOException("GetAllDataValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, AllDataValuesDao dao) throws IOException {
        CmsGetAllDataValuesResponse resp = CmsFrameDecoder.decodeResp(frame, new CmsGetAllDataValuesResponse());
        if (content() != null) {
            // Convert each entry's value to structured JSON
            List<Map<String, Object>> convertedData = new ArrayList<>();
            for (CmsDataValueEntry entry : resp.data) {
                Map<String, Object> entryMap = new LinkedHashMap<>();
                entryMap.put("reference", entry.reference.value());
                String jsonStr = entry.value.toValueString();
                if (jsonStr.startsWith("{")) {
                    try {
                        entryMap.put("value", MAPPER.readValue(jsonStr, Object.class));
                    } catch (Exception e) {
                        entryMap.put("value", jsonStr);
                    }
                } else {
                    entryMap.put("value", jsonStr);
                }
                convertedData.add(entryMap);
            }

            boolean moreFollows = resp.moreFollows.value();

            // Accumulate pages
            @SuppressWarnings("unchecked")
            Map<String, Object> resMap = (Map<String, Object>) content().res();
            if (resMap == null) {
                Map<String, Object> newMap = new LinkedHashMap<>();
                newMap.put("data", new ArrayList<>(convertedData));
                newMap.put("moreFollows", moreFollows ? 1 : 0);
                content().res(newMap);
            } else {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> existingData = (List<Map<String, Object>>) resMap.get("data");
                existingData.addAll(convertedData);
                resMap.put("moreFollows", moreFollows ? 1 : 0);
            }

            // Update pagination context
            PaginationContext ctx = content().paginationContext();
            ctx.setLastMoreFollows(moreFollows);
            if (!convertedData.isEmpty()) {
                Map<String, Object> lastItem = convertedData.get(convertedData.size() - 1);
                Object ref = lastItem.get("reference");
                ctx.setLastReference(ref != null ? ref.toString() : null);
            }
        }
    }

    @Override
    protected void setPaginationCursor(AllDataValuesDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
