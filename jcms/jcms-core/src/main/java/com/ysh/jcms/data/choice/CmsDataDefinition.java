package com.ysh.jcms.data.choice;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.core.CmsEnumerated;
import com.ysh.jcms.data.scalar.CmsInt32;
import java.util.Arrays;
import java.util.List;

/**
 *
 * DataDefinition ::= CHOICE { error [0] IMPLICIT ServiceError, array [1]
 * IMPLICIT SEQUENCE { numberOfElement [1] IMPLICIT Int32, elementType [2]
 * DataDefinition }, structure [2] IMPLICIT SEQUENCE OF SEQUENCE { name [0]
 * IMPLICIT ObjectName, fc [1] IMPLICIT FunctionalConstraint OPTIONAL, type [2]
 * DataDefinition }, boolean [3] IMPLICIT NULL, int8 [4] IMPLICIT NULL, int16
 * [5] IMPLICIT NULL, int32 [6] IMPLICIT NULL, int64 [7] IMPLICIT NULL, int8u
 * [8] IMPLICIT NULL, int16u [9] IMPLICIT NULL, int32u [10] IMPLICIT NULL,
 * int64u [11] IMPLICIT NULL, float32 [12] IMPLICIT NULL, float64 [13] IMPLICIT
 * NULL, bit-string [14] IMPLICIT INTEGER, octet-string [15] IMPLICIT INTEGER,
 * visible-string [16] IMPLICIT INTEGER, unicode-string [17] IMPLICIT INTEGER,
 * utc-time [18] IMPLICIT NULL, binary-time [19] IMPLICIT NULL, quality [20]
 * IMPLICIT NULL, dbpos [21] IMPLICIT NULL, tcmd [22] IMPLICIT NULL, check [23]
 * IMPLICIT NULL }
 *
 * DataDefinition ::= CHOICE { 24 alternatives } — 7.7 Only alternatives with
 * payload are stored as pointers: [0] error → CmsServiceError* [1] array →
 * CmsDataDefinitionArray* [2] structure →
 * CmsArray<CmsDataDefinitionStructElem>* [14] bit-string → CmsInt32* (max bit
 * length) [15] octet-string → CmsInt32* (max byte length) [16] visible-string →
 * CmsInt32* (max char length) [17] unicode-string → CmsInt32* (max char length)
 *
 * Alternatives [3..13] and [18..23] are IMPLICIT NULL (no data). Flat
 * all-pointer layout, matching cms_data_definition_t in C. Only alternatives
 * with payload have pointer fields; NULL alternatives are not stored.
 *
 * nativeSize = 8 × 8 = 64 bytes
 */
public class CmsDataDefinition extends CmsType {

    public CmsEnumerated choice; /* selector 0..23 */

    /* [0] */
    public CmsServiceError alt_error;

    /* [1] */
    public CmsDataDefinitionArray alt_array;

    /* [2] structure — SEQUENCE OF DataDefinitionStructElem */
    public CmsArray<CmsDataDefinitionStructElem> alt_structure;

    /* [14..17] max length */
    public CmsInt32 alt_bit_string_len;
    public CmsInt32 alt_octet_string_len;
    public CmsInt32 alt_visible_string_len;
    public CmsInt32 alt_unicode_string_len;

    public CmsDataDefinition() {
        super(Codec.DATA_DEFINITION);
        this.choice = new CmsEnumerated();
        this.alt_error = new CmsServiceError();
        this.alt_array = new CmsDataDefinitionArray();
        this.alt_structure = new CmsArray<>(CmsDataDefinitionStructElem.class);
        this.alt_structure.allocSize = 0; // 避免嵌套预分配导致栈溢出
        this.alt_bit_string_len = new CmsInt32();
        this.alt_octet_string_len = new CmsInt32();
        this.alt_visible_string_len = new CmsInt32();
        this.alt_unicode_string_len = new CmsInt32();
    }

    public CmsDataDefinition choice(int v) {
        this.choice.value(v);
        return this;
    }
    @Override
    public List<? extends CmsType> children() {
        // Must match cms_data_definition_t C struct slot order:
        // [0]=choice, [1]=error, [2]=array, [3]=structure, [4]=bit-string,
        // [5]=octet-string, [6]=visible-string, [7]=unicode-string
        return Arrays.asList(choice, alt_error, alt_array, alt_structure, alt_bit_string_len, alt_octet_string_len, alt_visible_string_len,
                alt_unicode_string_len);
    }

    private CmsType choiceChild() {
        switch (choice.value()) {
            case 0 :
                return null; // CHOICE_ERROR — not a real field
            case 1 :
                return alt_array;
            case 2 :
                return alt_structure;
            case 3 :
                return alt_bit_string_len;
            case 4 :
                return alt_octet_string_len;
            case 5 :
                return alt_visible_string_len;
            case 6 :
                return alt_unicode_string_len;
            default :
                return null;
        }
    }

    @Override
    protected List<? extends CmsType> resizeList() {
        CmsType child = choiceChild();
        if (child == null)
            return java.util.Collections.emptyList();
        return java.util.Collections.singletonList(child);
    }
}
