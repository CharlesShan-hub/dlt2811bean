package com.ysh.jcms.core.data.choice;

import com.ysh.jcms.core.data.sequence.common.CmsDataDefinitionArray;
import com.ysh.jcms.core.data.sequence.common.CmsDataDefinitionStructElem;
import com.ysh.jcms.core.data.core.CmsChoice;
import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerDataDefinition;
import com.ysh.jcms.data.V;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.scalar.CmsInt32;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * DataDefinition ::= CHOICE {
 *     error          [0] IMPLICIT ServiceError,
 *     array          [1] IMPLICIT SEQUENCE {
 *         numberOfElement  [1] IMPLICIT Int32,
 *         elementType      [2] DataDefinition
 *     },
 *     structure      [2] IMPLICIT SEQUENCE OF SEQUENCE {
 *         name             [0] IMPLICIT ObjectName,
 *         fc               [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *         type             [2] DataDefinition
 *     },
 *     boolean        [3] IMPLICIT NULL,
 *     int8           [4] IMPLICIT NULL,
 *     int16          [5] IMPLICIT NULL,
 *     int32          [6] IMPLICIT NULL,
 *     int64          [7] IMPLICIT NULL,
 *     int8u          [8] IMPLICIT NULL,
 *     int16u         [9] IMPLICIT NULL,
 *     int32u         [10] IMPLICIT NULL,
 *     int64u         [11] IMPLICIT NULL,
 *     float32        [12] IMPLICIT NULL,
 *     float64        [13] IMPLICIT NULL,
 *     bit-string     [14] IMPLICIT INTEGER,
 *     octet-string   [15] IMPLICIT INTEGER,
 *     visible-string [16] IMPLICIT INTEGER,
 *     unicode-string [17] IMPLICIT INTEGER,
 *     utc-time       [18] IMPLICIT NULL,
 *     binary-time    [19] IMPLICIT NULL,
 *     quality        [20] IMPLICIT NULL,
 *     dbpos          [21] IMPLICIT NULL,
 *     tcmd           [22] IMPLICIT NULL,
 *     check          [23] IMPLICIT NULL
 * } — 7.7.2
 * }
 * </pre>
 *
 * <p>
 * Only alternatives with payload are mapped: error (0), array (1), structure
 * (2), bit-string (14), octet-string (15), visible-string (16), unicode-string
 * (17). Alternatives [3..13] and [18..23] are NULL (no data).
 */
public class CmsDataDefinition extends CmsChoice {

    public static final int CHOICE_ERROR = 0;
    public static final int CHOICE_ARRAY = 1;
    public static final int CHOICE_STRUCTURE = 2;
    public static final int CHOICE_BOOLEAN = 3;
    public static final int CHOICE_INT8 = 4;
    public static final int CHOICE_INT16 = 5;
    public static final int CHOICE_INT32 = 6;
    public static final int CHOICE_INT64 = 7;
    public static final int CHOICE_INT8U = 8;
    public static final int CHOICE_INT16U = 9;
    public static final int CHOICE_INT32U = 10;
    public static final int CHOICE_INT64U = 11;
    public static final int CHOICE_FLOAT32 = 12;
    public static final int CHOICE_FLOAT64 = 13;
    public static final int CHOICE_BIT_STRING = 14;
    public static final int CHOICE_OCTET_STRING = 15;
    public static final int CHOICE_VISIBLE_STRING = 16;
    public static final int CHOICE_UNICODE_STRING = 17;
    public static final int CHOICE_UTC_TIME = 18;
    public static final int CHOICE_BINARY_TIME = 19;
    public static final int CHOICE_QUALITY = 20;
    public static final int CHOICE_DBPOS = 21;
    public static final int CHOICE_TCMD = 22;
    public static final int CHOICE_CHECK = 23;

    /* [0] — WRAPPER */
    @Choice(index = 0, name = "error", sync = Sync.WRAPPER)
    public CmsServiceError alt_error;

    /*
     * [1] array — manual (no @Choice: eager creation would recurse via
     * CmsDataDefinitionArray.elementType → new CmsDataDefinition())
     */
    public CmsDataDefinitionArray alt_array;

    /*
     * [2] structure — SEQUENCE OF DataDefinitionStructElem (manual, creates new
     * container each sync)
     */
    public List<CmsDataDefinitionStructElem> alt_structure;

    /* [14..17] CmsInt32 → Integer in inner (manual, no matching InnerBase field) */
    public CmsInt32 alt_bit_string_len;
    public CmsInt32 alt_octet_string_len;
    public CmsInt32 alt_visible_string_len;
    public CmsInt32 alt_unicode_string_len;

    public CmsDataDefinition() {
        super(new InnerDataDefinition());
        // NULL alternatives [3..13]
        registerNullChoice(3, "Boolean");
        registerNullChoice(4, "int8");
        registerNullChoice(5, "int16");
        registerNullChoice(6, "int32");
        registerNullChoice(7, "int64");
        registerNullChoice(8, "int8u");
        registerNullChoice(9, "int16u");
        registerNullChoice(10, "int32u");
        registerNullChoice(11, "int64u");
        registerNullChoice(12, "float32");
        registerNullChoice(13, "float64");
        // NULL alternatives [18..23]
        registerNullChoice(18, "utc-time");
        registerNullChoice(19, "binary-time");
        registerNullChoice(20, "quality");
        registerNullChoice(21, "dbpos");
        registerNullChoice(22, "tcmd");
        registerNullChoice(23, "check");

        this.alt_error = new CmsServiceError();
        this.alt_array = null; /* lazily created on CHOICE_ARRAY (see syncToInner) */
        this.alt_structure = new ArrayList<>();
        this.alt_bit_string_len = new CmsInt32();
        this.alt_octet_string_len = new CmsInt32();
        this.alt_visible_string_len = new CmsInt32();
        this.alt_unicode_string_len = new CmsInt32();
    }

    public CmsDataDefinition choice(int v) {
        super.choice(v);
        return this;
    }

    /* ─── Fluent setters ─── */
    public CmsDataDefinition alt_error(int v) {
        choice(CHOICE_ERROR);
        this.alt_error.value(v);
        return this;
    }
    public CmsDataDefinition alt_bit_string_len(int v) {
        choice(CHOICE_BIT_STRING);
        this.alt_bit_string_len.value(v);
        return this;
    }
    public CmsDataDefinition alt_octet_string_len(int v) {
        choice(CHOICE_OCTET_STRING);
        this.alt_octet_string_len.value(v);
        return this;
    }
    public CmsDataDefinition alt_visible_string_len(int v) {
        choice(CHOICE_VISIBLE_STRING);
        this.alt_visible_string_len.value(v);
        return this;
    }
    public CmsDataDefinition alt_unicode_string_len(int v) {
        choice(CHOICE_UNICODE_STRING);
        this.alt_unicode_string_len.value(v);
        return this;
    }

    /** Copy choice selection and value from another CmsDataDefinition (fluent). */
    public CmsDataDefinition value(CmsDataDefinition v) {
        int ch = v.choice();
        switch (ch) {
            case CHOICE_ERROR :
                return alt_error(v.alt_error.value());
            case CHOICE_ARRAY :
                this.alt_array = v.alt_array;
                return choice(ch);
            case CHOICE_BIT_STRING :
                return alt_bit_string_len(v.alt_bit_string_len.value());
            case CHOICE_OCTET_STRING :
                return alt_octet_string_len(v.alt_octet_string_len.value());
            case CHOICE_VISIBLE_STRING :
                return alt_visible_string_len(v.alt_visible_string_len.value());
            case CHOICE_UNICODE_STRING :
                return alt_unicode_string_len(v.alt_unicode_string_len.value());
            default :
                // NULL alternatives: structure (2), [3..13], [18..23] — choice only
                if (ch == CHOICE_STRUCTURE || (ch >= CHOICE_BOOLEAN && ch <= CHOICE_FLOAT64)
                        || (ch >= CHOICE_UTC_TIME && ch <= CHOICE_CHECK)) {
                    return choice(ch);
                }
                throw new IllegalArgumentException("Unknown DataDefinition choice: " + ch);
        }
    }

    @Override
    public void syncToInner() {
        int ch = choice();
        if (ch < 0)
            return;

        // Handle array manually — alt_array is created lazily to break the
        // ctor recursion CmsDataDefinition ↔ CmsDataDefinitionArray
        if (ch == CHOICE_ARRAY) {
            if (alt_array == null)
                alt_array = new CmsDataDefinitionArray();
            alt_array.syncToInner();
            inner._v.put("_choice", "array");
            inner._v.put("_", alt_array.inner._v);
            return;
        }

        // Handle structure manually — JER 中 structure 是 SEQUENCE OF（直接数组，
        // {"structure": [...]}），不能用 InnerDataDefinitionStructure 包 {"value": [...]}
        if (ch == CHOICE_STRUCTURE) {
            inner._v.put("_choice", "structure");
            List<InnerBase> list = new ArrayList<>();
            for (CmsDataDefinitionStructElem elem : alt_structure) {
                elem.syncToInner();
                list.add(elem.inner);
            }
            inner._v.put("_", list);
            return;
        }

        // Handle string length variants — CmsInt32 → Integer
        if (ch == CHOICE_BIT_STRING) {
            inner._v.put("_choice", "bit-string");
            inner._v.put("_", alt_bit_string_len.value());
            return;
        }
        if (ch == CHOICE_OCTET_STRING) {
            inner._v.put("_choice", "octet-string");
            inner._v.put("_", alt_octet_string_len.value());
            return;
        }
        if (ch == CHOICE_VISIBLE_STRING) {
            inner._v.put("_choice", "visible-string");
            inner._v.put("_", alt_visible_string_len.value());
            return;
        }
        if (ch == CHOICE_UNICODE_STRING) {
            inner._v.put("_choice", "unicode-string");
            inner._v.put("_", alt_unicode_string_len.value());
            return;
        }

        // Let base class handle error, array, and NULL variants
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        Object chObj = inner._v.get("_choice");
        if (!(chObj instanceof String)) {
            // Decoded _v holds JER form ({"structure": [...]}, {"Boolean": null}, …)
            // without _choice — pick the variant from the first non-metadata key and
            // move its payload into the "_" value slot. normalizeVariant() is NOT
            // safe here: it wraps non-map payloads (e.g. structure's List) into
            // {"_": ...}.
            for (java.util.Map.Entry<String, Object> e : inner._v.entrySet()) {
                if (e.getKey().startsWith("_"))
                    continue;
                chObj = e.getKey();
                V.setChoice(inner._v, e.getKey());
                inner._v.put("_", e.getValue());
                inner._v.remove(e.getKey());
                break;
            }
            if (!(chObj instanceof String))
                return;
        }
        String ch = (String) chObj;

        // Handle array manually
        if ("array".equals(ch)) {
            selectedChoiceIndex = CHOICE_ARRAY;
            Object sub = inner._v.get("_");
            if (sub instanceof java.util.LinkedHashMap) {
                alt_array = new CmsDataDefinitionArray();
                alt_array.inner._v = (java.util.LinkedHashMap<String, Object>) sub;
                alt_array.syncFromInner();
            }
            return;
        }

        // Handle structure manually — JER 中 structure 是数组
        if ("structure".equals(ch)) {
            selectedChoiceIndex = CHOICE_STRUCTURE;
            alt_structure.clear();
            Object structObj = inner._v.get("_");
            if (structObj instanceof List) {
                for (Object elem : (List<?>) structObj) {
                    CmsDataDefinitionStructElem c = new CmsDataDefinitionStructElem();
                    if (elem instanceof InnerBase) {
                        c.inner = (InnerBase) elem;
                    } else if (elem instanceof java.util.Map) {
                        // Jackson deserialized the SEQUENCE OF elements into raw
                        // maps — share them as the wrapper's _v and rebind the
                        // injected name/fc/type wrappers to the new map.
                        @SuppressWarnings("unchecked")
                        java.util.LinkedHashMap<String, Object> m = (java.util.LinkedHashMap<String, Object>) elem;
                        c.inner._v = m;
                        c.rebind();
                    } else {
                        continue;
                    }
                    c.syncFromInner();
                    alt_structure.add(c);
                }
            }
            return;
        }

        // Handle string length variants
        if ("bit-string".equals(ch)) {
            selectedChoiceIndex = CHOICE_BIT_STRING;
            Object v = inner._v.get("_");
            alt_bit_string_len.value(v instanceof Number ? ((Number) v).intValue() : 0);
            return;
        }
        if ("octet-string".equals(ch)) {
            selectedChoiceIndex = CHOICE_OCTET_STRING;
            Object v = inner._v.get("_");
            alt_octet_string_len.value(v instanceof Number ? ((Number) v).intValue() : 0);
            return;
        }
        if ("visible-string".equals(ch)) {
            selectedChoiceIndex = CHOICE_VISIBLE_STRING;
            Object v = inner._v.get("_");
            alt_visible_string_len.value(v instanceof Number ? ((Number) v).intValue() : 0);
            return;
        }
        if ("unicode-string".equals(ch)) {
            selectedChoiceIndex = CHOICE_UNICODE_STRING;
            Object v = inner._v.get("_");
            alt_unicode_string_len.value(v instanceof Number ? ((Number) v).intValue() : 0);
            return;
        }

        // Let base class handle error, array, and NULL variants
        super.syncFromInner();
    }
}
