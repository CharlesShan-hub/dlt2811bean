package com.ysh.jcms.app.handler.sg.setEditSgValue;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.sg.CmsSgRefValueEntry;
import com.ysh.jcms.pdu.sg.CmsSetEditSgValueRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for SetEditSGValue (8.6.3). Holds a list of (reference, value) pairs to
 * be sent to the server.
 */
@Setter
@Getter
@Accessors(fluent = true)
public class SetEditSgValueDao extends BaseDao {

    private static final Map<String, Integer> TYPE_MAP = buildTypeMap();
    private static Map<String, Integer> buildTypeMap() {
        Map<String, Integer> m = new HashMap<>();
        m.put("boolean", CmsData.CHOICE_BOOLEAN);
        m.put("int8", CmsData.CHOICE_INT8);
        m.put("int16", CmsData.CHOICE_INT16);
        m.put("int32", CmsData.CHOICE_INT32);
        m.put("int64", CmsData.CHOICE_INT64);
        m.put("int8u", CmsData.CHOICE_INT8U);
        m.put("int16u", CmsData.CHOICE_INT16U);
        m.put("int32u", CmsData.CHOICE_INT32U);
        m.put("int64u", CmsData.CHOICE_INT64U);
        m.put("float32", CmsData.CHOICE_FLOAT32);
        m.put("float64", CmsData.CHOICE_FLOAT64);
        m.put("visible-string", CmsData.CHOICE_VISIBLE_STRING);
        m.put("octet-string", CmsData.CHOICE_OCTET_STRING);
        return Collections.unmodifiableMap(m);
    }

    private List<String> refs = new ArrayList<>();
    private List<String> values = new ArrayList<>();
    private String type = "visible-string";

    private static int resolveType(String typeStr) {
        Integer choice = TYPE_MAP.get(typeStr);
        if (choice == null)
            throw new IllegalArgumentException("Unknown type: " + typeStr);
        return choice;
    }

    @Override
    public CmsType toRequest() {
        if (refs.size() != values.size())
            throw new IllegalStateException("--refs count (" + refs.size() + ") != --values count (" + values.size() + ")");

        int choiceType = resolveType(type);
        CmsSetEditSgValueRequest req = new CmsSetEditSgValueRequest();
        for (int i = 0; i < refs.size(); i++) {
            CmsData data = buildCmsData(values.get(i), choiceType);
            req.data.add(new CmsSgRefValueEntry().reference(refs.get(i)).value(data));
        }
        return req;
    }

    private static CmsData buildCmsData(String textVal, int choiceType) {
        CmsData data = new CmsData();
        switch (choiceType) {
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
