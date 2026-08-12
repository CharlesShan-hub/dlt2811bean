package com.ysh.jcms.app.handler.sg.getEditSgValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.pdu.sg.CmsGetEditSgValueError;
import com.ysh.jcms.core.pdu.sg.CmsGetEditSgValueResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.app.handler.BaseClientHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GetEditSgValueClient extends BaseClientHandler<GetEditSgValueDao> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void execute(GetEditSgValueDao dao) throws Exception {
        send(CmsServiceInfo.GET_EDIT_SG_VALUE, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetEditSgValueError err = decodeErr(frame, new CmsGetEditSgValueError());
        throw new IOException("GetEditSGValue rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetEditSgValueDao dao) throws IOException {
        CmsGetEditSgValueResponse resp = decodeResp(frame, new CmsGetEditSgValueResponse());
        if (content() != null) {
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
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("value", structuredValues);
            result.put("moreFollows", resp.moreFollows.value() ? 1 : 0);
            content().res(result);
        }
    }
}
