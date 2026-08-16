package com.ysh.jcms.app.handler.data.getDataValues;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.pdu.data.CmsGetDataValuesError;
import com.ysh.jcms.core.pdu.data.CmsGetDataValuesResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GetDataValuesClient extends BaseClientHandler<GetDataValuesDao> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void execute(GetDataValuesDao dao) throws Exception {
        send(CmsServiceInfo.GET_DATA_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataValuesError err = CmsFrameDecoder.decodeErr(frame, new CmsGetDataValuesError());
        throw new IOException("GetDataValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetDataValuesDao dao) throws IOException {
        CmsGetDataValuesResponse resp = CmsFrameDecoder.decodeResp(frame, new CmsGetDataValuesResponse());
        if (content() != null) {
            List<Object> structuredValues = new ArrayList<>();
            for (CmsData data : resp.value) {
                String jsonStr = data.toValueString();
                // Complex types (quality, utc-time, binary-time) return JSON strings
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
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("value", structuredValues);
            result.put("moreFollows", resp.moreFollows.value() ? 1 : 0);
            content().res(result);
        }
    }
}
