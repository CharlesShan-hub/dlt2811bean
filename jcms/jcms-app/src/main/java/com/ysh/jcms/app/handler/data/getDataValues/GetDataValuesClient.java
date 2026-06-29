package com.ysh.jcms.app.handler.data.getDataValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.svc.data.CmsDataRefEntry;
import com.ysh.jcms.svc.data.CmsGetDataValuesError;
import com.ysh.jcms.svc.data.CmsGetDataValuesRequest;
import com.ysh.jcms.svc.data.CmsGetDataValuesResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
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

    public GetDataValuesClient(CmsNode node) {
        super(node);
    }

    public List<DataValue> getLastValues() { return lastValues; }

    public void execute(GetDataValuesDao dao) throws Exception {
        CmsGetDataValuesRequest req = new CmsGetDataValuesRequest()
            .reqId(nextReqId());

        for (GetDataValuesDao.DataRef ref : dao.dataRefs()) {
            CmsDataRefEntry entry = new CmsDataRefEntry()
                .reference(ref.reference());
            if (ref.fc() != null && ref.fc() != 0) {
                entry.fcPresent(true);
                entry.fc(ref.fc());
            }
            req.data.add(entry);
        }

        send(ServiceName.GET_DATA_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataValuesError err = new CmsGetDataValuesError();
        err.decode(frame.asduBytes());
        throw new IOException("GetDataValues rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetDataValuesResponse resp = new CmsGetDataValuesResponse();
        resp.value.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();
        resp.decode(frame.asduBytes());
        traceResp(resp);

        List<DataValue> values = new ArrayList<>();
        for (int i = 0; i < resp.value.count; i++) {
            CmsData d = resp.value.items.get(i);
            values.add(new DataValue(d.choice.value(), extractValue(d)));
        }
        this.lastValues = values;
        log.info("GetDataValues succeeded: {} values", values.size());
    }

    private static String extractValue(CmsData d) {
        int ct = d.choice.value();
        switch (ct) {
            case CmsData.CHOICE_BOOLEAN:       return Boolean.toString(d.alt_boolean.value());
            case CmsData.CHOICE_INT8:           return Integer.toString(d.alt_int8.value());
            case CmsData.CHOICE_INT16:          return Integer.toString(d.alt_int16.value());
            case CmsData.CHOICE_INT32:          return Integer.toString(d.alt_int32.value());
            case CmsData.CHOICE_INT64:          return Long.toString(d.alt_int64.value());
            case CmsData.CHOICE_INT8U:          return Integer.toString(d.alt_int8u.value());
            case CmsData.CHOICE_INT16U:         return Integer.toString(d.alt_int16u.value());
            case CmsData.CHOICE_INT32U:         return Long.toString(d.alt_int32u.value());
            case CmsData.CHOICE_INT64U:         return d.alt_int64u.value().toString();
            case CmsData.CHOICE_FLOAT32:        return Float.toString(d.alt_float32.value());
            case CmsData.CHOICE_FLOAT64:        return Double.toString(d.alt_float64.value());
            case CmsData.CHOICE_VISIBLE_STRING:
                return new String(d.alt_visible_string.value(), StandardCharsets.UTF_8);
            case CmsData.CHOICE_UNICODE_STRING:
                return new String(d.alt_unicode_string.value(), StandardCharsets.UTF_8);
            case CmsData.CHOICE_OCTET_STRING:
                return bytesToHex(d.alt_octet_string.value());
            case CmsData.CHOICE_BIT_STRING:
                return new String(d.alt_bit_string.value(), StandardCharsets.UTF_8);
            default:                            return "(choice=" + ct + ")";
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02X", b & 0xFF));
        return sb.toString();
    }
}
