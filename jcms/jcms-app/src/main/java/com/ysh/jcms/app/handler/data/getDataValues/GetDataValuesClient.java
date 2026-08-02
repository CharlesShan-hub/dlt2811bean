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
import java.nio.charset.StandardCharsets;
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
            values.add(new DataValue(d.choice(), extractValue(d)));
        }
        this.lastValues = values;
        log.info("GetDataValues succeeded: {} values", values.size());
    }

    private static String extractValue(CmsData d) {
        int ct = d.choice();
        switch (ct) {
            case CmsData.CHOICE_BOOLEAN :
                return Boolean.toString(d.alt_boolean.value());
            case CmsData.CHOICE_INT8 :
                return Integer.toString(d.alt_int8.value());
            case CmsData.CHOICE_INT16 :
                return Integer.toString(d.alt_int16.value());
            case CmsData.CHOICE_INT32 :
                return Integer.toString(d.alt_int32.value());
            case CmsData.CHOICE_INT64 :
                return Long.toString(d.alt_int64.value());
            case CmsData.CHOICE_INT8U :
                return Integer.toString(d.alt_int8u.value());
            case CmsData.CHOICE_INT16U :
                return Integer.toString(d.alt_int16u.value());
            case CmsData.CHOICE_INT32U :
                return Long.toString(d.alt_int32u.value());
            case CmsData.CHOICE_INT64U :
                return d.alt_int64u.value().toString();
            case CmsData.CHOICE_FLOAT32 :
                return Float.toString(d.alt_float32.value());
            case CmsData.CHOICE_FLOAT64 :
                return Double.toString(d.alt_float64.value());
            case CmsData.CHOICE_VISIBLE_STRING :
                return (String) d.alt_visible_string.toJsonValue();
            case CmsData.CHOICE_UNICODE_STRING :
                return (String) d.alt_unicode_string.toJsonValue();
            case CmsData.CHOICE_OCTET_STRING :
                return (String) d.alt_octet_string.toJsonValue();
            case CmsData.CHOICE_BIT_STRING :
                return new String(d.alt_bit_string, StandardCharsets.UTF_8);
            default :
                return "(choice=" + ct + ")";
        }
    }
}
