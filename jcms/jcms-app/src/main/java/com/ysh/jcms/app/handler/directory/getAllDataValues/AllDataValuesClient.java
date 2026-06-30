package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.app.node.ContentManager;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.svc.directory.CmsGetAllDataValuesError;
import com.ysh.jcms.svc.directory.CmsGetAllDataValuesRequest;
import com.ysh.jcms.svc.directory.CmsGetAllDataValuesResponse;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllDataValuesClient extends BaseClientHandler {

    public AllDataValuesClient(CmsNode node) {
        super(node);
    }

    public void execute(AllDataValuesDao dao) throws Exception {
        CmsGetAllDataValuesRequest req = new CmsGetAllDataValuesRequest()
            .reqId(nextReqId());

        if (dao.ldName() != null) {
            req.reference.choice(CmsReferenceChoice.LD_NAME);
            req.reference.altLdName.value(dao.ldName());
        } else if (dao.lnReference() != null) {
            req.reference.choice(CmsReferenceChoice.LN_REFERENCE);
            req.reference.altLnReference.value(dao.lnReference());
        }

        if (dao.fc() != null) {
            req.fc(dao.fc());
        }

        if (dao.referenceAfter() != null) {
            req.refAfter(dao.referenceAfter());
        }

        send(ServiceName.GET_ALL_DATA_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllDataValuesError err = new CmsGetAllDataValuesError();
        err.decode(frame.asduBytes());
        throw new IOException("GetAllDataValues rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetAllDataValuesResponse resp = new CmsGetAllDataValuesResponse();
        resp.data.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();
        resp.decode(frame.asduBytes());
        traceResp(resp);

        List<ContentManager.AllDataEntry> entries = new ArrayList<>();
        for (int i = 0; i < resp.data.count; i++) {
            CmsDataValueEntryWrap entry = new CmsDataValueEntryWrap(resp.data.items.get(i));
            if (entry.choiceType == 0) continue; // skip error/empty entries
            entries.add(new ContentManager.AllDataEntry(
                entry.reference,
                entry.choiceType,
                entry.valueString
            ));
        }
        node.getContentManager().initAllData(entries);
        log.info("GetAllDataValues succeeded: {} entries", entries.size());
    }

    /** Wraps CmsDataValueEntry to extract readable values after native decode. */
    private static class CmsDataValueEntryWrap {
        final String reference;
        final int choiceType;
        final String valueString;

        CmsDataValueEntryWrap(com.ysh.jcms.svc.directory.CmsDataValueEntry e) {
            this.reference = new String(e.reference.value());
            int ct = e.value.choice.value();
            this.choiceType = ct;
            this.valueString = extractValue(e.value, ct);
        }

        private static String extractValue(CmsData d, int ct) {
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
                    return new String(d.alt_visible_string.value(), java.nio.charset.StandardCharsets.UTF_8);
                case CmsData.CHOICE_UNICODE_STRING:
                    return new String(d.alt_unicode_string.value(), java.nio.charset.StandardCharsets.UTF_8);
                case CmsData.CHOICE_OCTET_STRING:
                    return new String(d.alt_octet_string.value(), java.nio.charset.StandardCharsets.UTF_8);
                case CmsData.CHOICE_BIT_STRING:
                    return new String(d.alt_bit_string.value(), java.nio.charset.StandardCharsets.UTF_8);
                default:                            return "(choice=" + ct + ")";
            }
        }

        // private static String bytesToHex(byte[] bytes) {
        //     StringBuilder sb = new StringBuilder(bytes.length * 2);
        //     for (byte b : bytes) sb.append(String.format("%02X", b & 0xFF));
        //     return sb.toString();
        // }
    }
}
