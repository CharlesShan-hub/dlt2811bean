package com.ysh.jcms.app.handler.sg.setEditSgValue;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.sg.CmsSetEditSgValueError;
import com.ysh.jcms.pdu.sg.CmsSetEditSgValueRequest;
import com.ysh.jcms.pdu.sg.CmsSetEditSgValueResponse;
import com.ysh.jcms.data.sequence.sg.CmsSgRefValueEntry;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SetEditSgValueClient extends BaseClientHandler {

    public void execute(SetEditSgValueDao dao) throws Exception {
        CmsSetEditSgValueRequest req = new CmsSetEditSgValueRequest();

        for (SetEditSgValueDao.Entry entry : dao.entries()) {
            CmsData data = buildCmsData(entry);
            req.data.add(new CmsSgRefValueEntry().reference(entry.ref()).value(data));
        }

        send(ServiceName.SET_EDIT_SG_VALUE, req);
    }

    private static CmsData buildCmsData(SetEditSgValueDao.Entry entry) {
        CmsData data = new CmsData();
        String textVal = new String(entry.valueBytes(), StandardCharsets.UTF_8);
        switch (entry.choiceType()) {
            case CmsData.CHOICE_BOOLEAN :
                data.alt_boolean("true".equalsIgnoreCase(textVal) || "1".equals(textVal));
                break;
            case CmsData.CHOICE_INT8 :
                data.alt_int8(Byte.parseByte(textVal));
                break;
            case CmsData.CHOICE_INT16 :
                data.alt_int16(Short.parseShort(textVal));
                break;
            case CmsData.CHOICE_INT32 :
                data.alt_int32(Integer.parseInt(textVal));
                break;
            case CmsData.CHOICE_INT64 :
                data.alt_int64(Long.parseLong(textVal));
                break;
            case CmsData.CHOICE_INT8U :
                data.alt_int8u(Integer.parseInt(textVal) & 0xFF);
                break;
            case CmsData.CHOICE_INT16U :
                data.alt_int16u(Integer.parseInt(textVal) & 0xFFFF);
                break;
            case CmsData.CHOICE_INT32U :
                data.alt_int32u(Long.parseLong(textVal) & 0xFFFFFFFFL);
                break;
            case CmsData.CHOICE_INT64U :
                data.alt_int64u(new java.math.BigInteger(textVal));
                break;
            case CmsData.CHOICE_FLOAT32 :
                data.alt_float32(Float.parseFloat(textVal));
                break;
            case CmsData.CHOICE_FLOAT64 :
                data.alt_float64(Double.parseDouble(textVal));
                break;
            case CmsData.CHOICE_VISIBLE_STRING :
            default :
                data.alt_visible_string(textVal);
                break;
        }
        return data;
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetEditSgValueError err = decodeErr(frame, new CmsSetEditSgValueError());
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (CmsServiceError e : err.result) {
            if (sb.length() > 0)
                sb.append(", ");
            sb.append("entry[").append(i).append("]=").append(e.value());
            i++;
        }
        throw new IOException("SetEditSGValue rejected: " + sb);
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetEditSgValueResponse resp = decodeResp(frame, new CmsSetEditSgValueResponse());
        log.info("SetEditSGValue succeeded");
    }
}
