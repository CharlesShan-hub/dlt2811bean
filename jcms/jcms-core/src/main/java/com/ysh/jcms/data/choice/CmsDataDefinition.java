package com.ysh.jcms.data.choice;

import com.ysh.jcms.core.CmsType;
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
public class CmsDataDefinition extends CmsType {

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

    public int choice; /* selector 0..23 */

    /* [0] */
    public CmsServiceError alt_error;

    /* [1] */
    public CmsDataDefinitionArray alt_array;

    /* [2] structure — SEQUENCE OF DataDefinitionStructElem */
    public List<CmsDataDefinitionStructElem> alt_structure;

    /* [14..17] max length */
    public CmsInt32 alt_bit_string_len;
    public CmsInt32 alt_octet_string_len;
    public CmsInt32 alt_visible_string_len;
    public CmsInt32 alt_unicode_string_len;

    public CmsDataDefinition() {
        super(new InnerDataDefinition());
        this.alt_error = new CmsServiceError();
        this.alt_array = new CmsDataDefinitionArray();
        this.alt_structure = new ArrayList<>();
        this.alt_bit_string_len = new CmsInt32();
        this.alt_octet_string_len = new CmsInt32();
        this.alt_visible_string_len = new CmsInt32();
        this.alt_unicode_string_len = new CmsInt32();
    }

    public CmsDataDefinition choice(int v) { this.choice = v; return this; }

    @Override
    public void syncToInner() {
        InnerDataDefinition i = (InnerDataDefinition) inner;
        switch (choice) {
            case CHOICE_ERROR:
                i._choice = "error";
                alt_error.syncToInner();
                i.error = (InnerServiceError) alt_error.inner;
                break;
            case CHOICE_ARRAY:
                i._choice = "array";
                alt_array.syncToInner();
                i.array = (InnerDataDefinitionArray) alt_array.inner;
                break;
            case CHOICE_STRUCTURE:
                i._choice = "structure";
                InnerDataDefinitionStructure innerStruct = new InnerDataDefinitionStructure();
                List<InnerAnonymousDataDefinitionStructure> list = new ArrayList<>();
                for (CmsDataDefinitionStructElem elem : alt_structure) {
                    elem.syncToInner();
                    list.add((InnerAnonymousDataDefinitionStructure) elem.inner);
                }
                innerStruct.value = list;
                i.structure = innerStruct;
                break;
            case CHOICE_BIT_STRING:
                i._choice = "bit-string";
                i.bit_string = alt_bit_string_len.value();
                break;
            case CHOICE_OCTET_STRING:
                i._choice = "octet-string";
                i.octet_string = alt_octet_string_len.value();
                break;
            case CHOICE_VISIBLE_STRING:
                i._choice = "visible-string";
                i.visible_string = alt_visible_string_len.value();
                break;
            case CHOICE_UNICODE_STRING:
                i._choice = "unicode-string";
                i.unicode_string = alt_unicode_string_len.value();
                break;
            default:
                // NULL alternatives: set _choice but no payload
                String[] names = { "Boolean", "int8", "int16", "int32", "int64",
                    "int8u", "int16u", "int32u", "int64u", "float32", "float64",
                    "utc-time", "binary-time", "quality", "dbpos", "tcmd", "check" };
                int[] nullChoices = { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
                    18, 19, 20, 21, 22, 23 };
                for (int j = 0; j < nullChoices.length; j++) {
                    if (choice == nullChoices[j]) {
                        i._choice = names[j];
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void syncFromInner() {
        InnerDataDefinition i = (InnerDataDefinition) inner;
        String ch = i._choice;
        if (ch == null) { choice = -1; return; }
        switch (ch) {
            case "error":
                choice = CHOICE_ERROR;
                alt_error.inner = i.error;
                alt_error.syncFromInner();
                break;
            case "array":
                choice = CHOICE_ARRAY;
                alt_array.inner = i.array;
                alt_array.syncFromInner();
                break;
            case "structure":
                choice = CHOICE_STRUCTURE;
                alt_structure.clear();
                if (i.structure != null) {
                    for (InnerAnonymousDataDefinitionStructure elem : i.structure.value) {
                        CmsDataDefinitionStructElem c = new CmsDataDefinitionStructElem();
                        c.inner = elem;
                        c.syncFromInner();
                        alt_structure.add(c);
                    }
                }
                break;
            case "bit-string":
                choice = CHOICE_BIT_STRING;
                alt_bit_string_len.value(i.bit_string);
                break;
            case "octet-string":
                choice = CHOICE_OCTET_STRING;
                alt_octet_string_len.value(i.octet_string);
                break;
            case "visible-string":
                choice = CHOICE_VISIBLE_STRING;
                alt_visible_string_len.value(i.visible_string);
                break;
            case "unicode-string":
                choice = CHOICE_UNICODE_STRING;
                alt_unicode_string_len.value(i.unicode_string);
                break;
            default:
                // NULL alternatives
                String[] names = { "Boolean", "int8", "int16", "int32", "int64",
                    "int8u", "int16u", "int32u", "int64u", "float32", "float64",
                    "utc-time", "binary-time", "quality", "dbpos", "tcmd", "check" };
                int[] nullChoices = { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
                    18, 19, 20, 21, 22, 23 };
                choice = -1;
                for (int j = 0; j < names.length; j++) {
                    if (names[j].equals(ch)) {
                        choice = nullChoices[j];
                        break;
                    }
                }
                break;
        }
    }
}
