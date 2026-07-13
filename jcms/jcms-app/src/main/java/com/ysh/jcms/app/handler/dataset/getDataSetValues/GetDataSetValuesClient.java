package com.ysh.jcms.app.handler.dataset.getDataSetValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.svc.dataset.CmsGetDataSetValuesError;
import com.ysh.jcms.svc.dataset.CmsGetDataSetValuesRequest;
import com.ysh.jcms.svc.dataset.CmsGetDataSetValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GetDataSetValuesClient extends BaseClientHandler {

    public static final class DataSetValue {
        public final int choiceType;
        public final String valueString;

        public DataSetValue(int choiceType, String valueString) {
            this.choiceType = choiceType;
            this.valueString = valueString;
        }
    }

    private static final String[] CHOICE_NAMES = {"error", "array", "structure", "boolean", "int8", "int16", "int32", "int64", "int8u",
            "int16u", "int32u", "int64u", "float32", "float64", "bit-string", "octet-string", "visible-string", "unicode-string",
            "utc-time", "binary-time", "quality", "dbpos", "tcmd", "check"};

    private List<DataSetValue> lastValues = new ArrayList<>();

    public List<DataSetValue> getLastValues() {
        return lastValues;
    }

    public void execute(GetDataSetValuesDao dao) throws Exception {
        CmsGetDataSetValuesRequest req = new CmsGetDataSetValuesRequest().reqId(nextReqId()).datasetReference(dao.datasetReference())
                .refAfter(dao.referenceAfter());

        send(ServiceName.GET_DATA_SET_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataSetValuesError err = decodeErr(frame, new CmsGetDataSetValuesError());
        throw new IOException("GetDataSetValues rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetDataSetValuesResponse resp = decodeResp(frame, new CmsGetDataSetValuesResponse());

        List<DataSetValue> entries = new ArrayList<>();
        for (int i = 0; i < resp.value.count; i++) {
            CmsData src = resp.value.items.get(i);
            int ct = src.choice.value();
            if (ct == 0)
                continue;
            String val = extractValue(src, ct);
            entries.add(new DataSetValue(ct, val));
        }
        this.lastValues = entries;
        log.info("GetDataSetValues succeeded: {} values", entries.size());
    }

    private static String extractValue(CmsData d, int ct) {
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
                return new String(d.alt_visible_string.value(), StandardCharsets.UTF_8);
            case CmsData.CHOICE_UNICODE_STRING :
                return new String(d.alt_unicode_string.value(), StandardCharsets.UTF_8);
            case CmsData.CHOICE_OCTET_STRING :
                return new String(d.alt_octet_string.value(), StandardCharsets.UTF_8);
            case CmsData.CHOICE_BIT_STRING :
                return new String(d.alt_bit_string.value(), StandardCharsets.UTF_8);
            default :
                return "(choice=" + ct + ")";
        }
    }
}
