package com.ysh.jcms.app.handler.sg.setEditSgValue;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.svc.sg.CmsSetEditSgValueError;
import com.ysh.jcms.svc.sg.CmsSetEditSgValueRequest;
import com.ysh.jcms.svc.sg.CmsSetEditSgValueResponse;
import com.ysh.jcms.svc.sg.CmsSgRefValueEntry;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SetEditSgValueClient extends BaseClientHandler {

    public SetEditSgValueClient(CmsNode node) {
        super(node);
    }

    public void execute(SetEditSgValueDao dao) throws Exception {
        CmsSetEditSgValueRequest req = new CmsSetEditSgValueRequest()
            .reqId(nextReqId());

        for (SetEditSgValueDao.Entry entry : dao.entries()) {
            CmsData data = buildCmsData(entry);
            req.data.add(new CmsSgRefValueEntry()
                .reference(entry.ref())
                .value(data));
        }

        send(ServiceName.SET_EDIT_SG_VALUE, req);
    }

    private static CmsData buildCmsData(SetEditSgValueDao.Entry entry) {
        CmsData data = new CmsData();
        String textVal = new String(entry.valueBytes(), StandardCharsets.UTF_8);
        data.choice(entry.choiceType());
        switch (entry.choiceType()) {
            case CmsData.CHOICE_BOOLEAN:
                data.alt_boolean.value("true".equalsIgnoreCase(textVal) || "1".equals(textVal));
                break;
            case CmsData.CHOICE_INT8:
                data.alt_int8.value(Byte.parseByte(textVal));
                break;
            case CmsData.CHOICE_INT16:
                data.alt_int16.value(Short.parseShort(textVal));
                break;
            case CmsData.CHOICE_INT32:
                data.alt_int32.value(Integer.parseInt(textVal));
                break;
            case CmsData.CHOICE_INT64:
                data.alt_int64.value(Long.parseLong(textVal));
                break;
            case CmsData.CHOICE_INT8U:
                data.alt_int8u.value(Integer.parseInt(textVal) & 0xFF);
                break;
            case CmsData.CHOICE_INT16U:
                data.alt_int16u.value(Integer.parseInt(textVal) & 0xFFFF);
                break;
            case CmsData.CHOICE_INT32U:
                data.alt_int32u.value(Long.parseLong(textVal) & 0xFFFFFFFFL);
                break;
            case CmsData.CHOICE_INT64U:
                data.alt_int64u.value(new java.math.BigInteger(textVal));
                break;
            case CmsData.CHOICE_FLOAT32:
                data.alt_float32.value(Float.parseFloat(textVal));
                break;
            case CmsData.CHOICE_FLOAT64:
                data.alt_float64.value(Double.parseDouble(textVal));
                break;
            case CmsData.CHOICE_VISIBLE_STRING:
            default:
                data.alt_visible_string.value(entry.valueBytes());
                break;
        }
        return data;
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetEditSgValueError err = new CmsSetEditSgValueError();
        err.decode(frame.asduBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < err.result.count; i++) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("entry[").append(i).append("]=").append(err.result.items.get(i).value());
        }
        throw new IOException("SetEditSGValue rejected: " + sb);
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetEditSgValueResponse resp = new CmsSetEditSgValueResponse();
        resp.decode(frame.asduBytes());
        traceResp(resp);
        log.info("SetEditSGValue succeeded");
    }
}
