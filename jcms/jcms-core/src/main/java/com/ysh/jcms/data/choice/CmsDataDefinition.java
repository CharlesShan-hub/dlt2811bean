package com.ysh.jcms.data.choice;

import com.ysh.jcms.core.CmsChoice;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsInt32;
import java.util.ArrayList;
import java.util.List;

/**
 * DataDefinition ::= CHOICE { 24 alternatives } — 7.7
 * <p>
 * Only alternatives with payload are mapped: error (0), array (1), structure
 * (2), bit-string (14), octet-string (15), visible-string (16),
 * unicode-string (17). Alternatives [3..13] and [18..23] are NULL (no data).
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

    /* [1] — WRAPPER */
    @Choice(index = 1, name = "array", sync = Sync.WRAPPER)
    public CmsDataDefinitionArray alt_array;

    /* [2] structure — SEQUENCE OF DataDefinitionStructElem (manual, creates new container each sync) */
    public List<CmsDataDefinitionStructElem> alt_structure;

    /* [14..17] CmsInt32 → Integer in inner (manual, no matching InnerBase field) */
    public CmsInt32 alt_bit_string_len;
    public CmsInt32 alt_octet_string_len;
    public CmsInt32 alt_visible_string_len;
    public CmsInt32 alt_unicode_string_len;

    public CmsDataDefinition() {
        super(new InnerDataDefinition());
        // NULL alternatives [3..13]
        registerNullChoice(3,  "Boolean");
        registerNullChoice(4,  "int8");
        registerNullChoice(5,  "int16");
        registerNullChoice(6,  "int32");
        registerNullChoice(7,  "int64");
        registerNullChoice(8,  "int8u");
        registerNullChoice(9,  "int16u");
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
        this.alt_array = new CmsDataDefinitionArray();
        this.alt_structure = new ArrayList<>();
        this.alt_bit_string_len = new CmsInt32();
        this.alt_octet_string_len = new CmsInt32();
        this.alt_visible_string_len = new CmsInt32();
        this.alt_unicode_string_len = new CmsInt32();
    }

    public CmsDataDefinition choice(int v) { super.choice(v); return this; }

    /* ─── Fluent setters ─── */
    public CmsDataDefinition alt_error(int v) { choice(CHOICE_ERROR); this.alt_error.value(v); return this; }
    public CmsDataDefinition alt_bit_string_len(int v) { choice(CHOICE_BIT_STRING); this.alt_bit_string_len.value(v); return this; }
    public CmsDataDefinition alt_octet_string_len(int v) { choice(CHOICE_OCTET_STRING); this.alt_octet_string_len.value(v); return this; }
    public CmsDataDefinition alt_visible_string_len(int v) { choice(CHOICE_VISIBLE_STRING); this.alt_visible_string_len.value(v); return this; }
    public CmsDataDefinition alt_unicode_string_len(int v) { choice(CHOICE_UNICODE_STRING); this.alt_unicode_string_len.value(v); return this; }

    @Override
    public void syncToInner() {
        int ch = choice();
        if (ch < 0) return;
        InnerDataDefinition i = (InnerDataDefinition) inner;

        // Handle structure manually — original creates new container each sync
        if (ch == CHOICE_STRUCTURE) {
            i._choice = "structure";
            InnerDataDefinitionStructure innerStruct = new InnerDataDefinitionStructure();
            List<InnerAnonymousDataDefinitionStructure> list = new ArrayList<>();
            for (CmsDataDefinitionStructElem elem : alt_structure) {
                elem.syncToInner();
                list.add((InnerAnonymousDataDefinitionStructure) elem.inner);
            }
            innerStruct.value = list;
            i.structure = innerStruct;
            return;
        }

        // Handle string length variants — CmsInt32 → Integer
        if (ch == CHOICE_BIT_STRING) {
            i._choice = "bit-string";
            i.bit_string = alt_bit_string_len.value();
            return;
        }
        if (ch == CHOICE_OCTET_STRING) {
            i._choice = "octet-string";
            i.octet_string = alt_octet_string_len.value();
            return;
        }
        if (ch == CHOICE_VISIBLE_STRING) {
            i._choice = "visible-string";
            i.visible_string = alt_visible_string_len.value();
            return;
        }
        if (ch == CHOICE_UNICODE_STRING) {
            i._choice = "unicode-string";
            i.unicode_string = alt_unicode_string_len.value();
            return;
        }

        // Let base class handle error, array, and NULL variants
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        InnerDataDefinition i = (InnerDataDefinition) inner;
        String ch = i._choice;
        if (ch == null) { innerCache.put("choice", -1); return; }

        // Handle structure manually
        if ("structure".equals(ch)) {
            innerCache.put("choice", CHOICE_STRUCTURE);
            alt_structure.clear();
            if (i.structure != null && i.structure.value != null) {
                for (InnerAnonymousDataDefinitionStructure elem : i.structure.value) {
                    CmsDataDefinitionStructElem c = new CmsDataDefinitionStructElem();
                    c.inner = elem;
                    c.syncFromInner();
                    alt_structure.add(c);
                }
            }
            return;
        }

        // Handle string length variants
        if ("bit-string".equals(ch)) {
            innerCache.put("choice", CHOICE_BIT_STRING);
            alt_bit_string_len.value(i.bit_string);
            return;
        }
        if ("octet-string".equals(ch)) {
            innerCache.put("choice", CHOICE_OCTET_STRING);
            alt_octet_string_len.value(i.octet_string);
            return;
        }
        if ("visible-string".equals(ch)) {
            innerCache.put("choice", CHOICE_VISIBLE_STRING);
            alt_visible_string_len.value(i.visible_string);
            return;
        }
        if ("unicode-string".equals(ch)) {
            innerCache.put("choice", CHOICE_UNICODE_STRING);
            alt_unicode_string_len.value(i.unicode_string);
            return;
        }

        // Let base class handle error, array, and NULL variants
        super.syncFromInner();
    }
}
