package com.ysh.jcms.app.handler.sg.setEditSgValue;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.sg.CmsSgRefValueEntry;
import com.ysh.jcms.pdu.sg.CmsSetEditSgValueRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for SetEditSGValue (8.6.3). Holds a list of (reference, value) pairs to
 * be sent to the server.
 */
@Setter
@Getter
@Accessors(fluent = true)
public class SetEditSgValueDao extends BaseDao {

    private final List<Entry> entries = new ArrayList<>();

    public SetEditSgValueDao addEntry(String ref, byte[] valueBytes, int choiceType) {
        entries.add(new Entry(ref, valueBytes, choiceType));
        return this;
    }

    public static class Entry {
        private final String ref;
        private final byte[] valueBytes;
        private final int choiceType;

        public Entry(String ref, byte[] valueBytes, int choiceType) {
            this.ref = ref;
            this.valueBytes = valueBytes;
            this.choiceType = choiceType;
        }

        public String ref() {
            return ref;
        }
        public byte[] valueBytes() {
            return valueBytes;
        }
        public int choiceType() {
            return choiceType;
        }
    }

    @Override
    public CmsType toRequest() {
        CmsSetEditSgValueRequest req = new CmsSetEditSgValueRequest();
        for (Entry entry : entries) {
            CmsData data = buildCmsData(entry);
            req.data.add(new CmsSgRefValueEntry().reference(entry.ref()).value(data));
        }
        return req;
    }

    private static CmsData buildCmsData(Entry entry) {
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
}
