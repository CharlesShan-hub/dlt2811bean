package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.ContentManager;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.sequence.directory.CmsDataValueEntry;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesError;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesRequest;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllDataValuesClient extends BaseClientHandler {

    public void execute(AllDataValuesDao dao) throws Exception {
        CmsGetAllDataValuesRequest req = new CmsGetAllDataValuesRequest().referenceAfter(dao.referenceAfter());

        if (dao.ldName() != null) {
            req.reference.altLdName(dao.ldName());
        } else if (dao.lnReference() != null) {
            req.reference.altLnReference(dao.lnReference());
        }

        if (dao.fc() != null) {
            req.fc(dao.fc());
        }

        send(ServiceName.GET_ALL_DATA_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllDataValuesError err = decodeErr(frame, new CmsGetAllDataValuesError());
        throw new IOException("GetAllDataValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetAllDataValuesResponse resp = decodeResp(frame, new CmsGetAllDataValuesResponse());

        List<ContentManager.AllDataEntry> entries = new ArrayList<>();
        for (CmsDataValueEntry e : resp.data) {
            CmsDataValueEntryWrap entry = new CmsDataValueEntryWrap(e);
            if (entry.choiceType == 0)
                continue; // skip error/empty entries
            entries.add(new ContentManager.AllDataEntry(entry.reference, entry.choiceType, entry.valueString));
        }
        node.getContentManager().initAllData(entries);
        log.info("GetAllDataValues succeeded: {} entries", entries.size());
    }

    /** Wraps CmsDataValueEntry to extract readable values after native decode. */
    private static class CmsDataValueEntryWrap {
        final String reference;
        final int choiceType;
        final String valueString;

        CmsDataValueEntryWrap(CmsDataValueEntry e) {
            this.reference = e.reference.value();
            int ct = e.value.choice();
            this.choiceType = ct;
            this.valueString = extractValue(e.value, ct);
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
                    return (String) d.alt_visible_string.toJsonValue();
                case CmsData.CHOICE_UNICODE_STRING :
                    return (String) d.alt_unicode_string.toJsonValue();
                case CmsData.CHOICE_OCTET_STRING :
                    return (String) d.alt_octet_string.toJsonValue();
                case CmsData.CHOICE_BIT_STRING :
                    return new String(d.alt_bit_string, java.nio.charset.StandardCharsets.UTF_8);
                default :
                    return "(choice=" + ct + ")";
            }
        }

        // private static String bytesToHex(byte[] bytes) {
        // StringBuilder sb = new StringBuilder(bytes.length * 2);
        // for (byte b : bytes) sb.append(String.format("%02X", b & 0xFF));
        // return sb.toString();
        // }
    }
}
