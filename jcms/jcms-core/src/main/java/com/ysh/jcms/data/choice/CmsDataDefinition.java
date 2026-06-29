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
 * DataDefinition ::= CHOICE { 24 alternatives }  —  7.7
 * Only alternatives with payload are stored as pointers:
 *   [0]  error              → CmsServiceError*
 *   [1]  array              → CmsDataDefinitionArray*
 *   [2]  structure          → CmsArray<CmsDataDefinitionStructElem>*
 *   [14] bit-string         → CmsInt32* (max bit length)
 *   [15] octet-string       → CmsInt32* (max byte length)
 *   [16] visible-string     → CmsInt32* (max char length)
 *   [17] unicode-string     → CmsInt32* (max char length)
 *
 * Alternatives [3..13] and [18..23] are IMPLICIT NULL (no data).
 * Flat all-pointer layout, matching cms_data_definition_t in C.
 * Only alternatives with payload have pointer fields; NULL alternatives
 * are not stored.
 *
 * nativeSize = 8 × 8 = 64 bytes
 */
public class CmsDataDefinition extends CmsType {

    public CmsEnumerated                        choice;              /* selector 0..23 */

    /* [0] */
    public CmsServiceError                      alt_error;

    /* [1] */
    public CmsDataDefinitionArray               alt_array;

    /* [2] structure — SEQUENCE OF DataDefinitionStructElem */
    public CmsArray<CmsDataDefinitionStructElem> alt_structure;

    /* [14..17] max length */
    public CmsInt32                             alt_bit_string_len;
    public CmsInt32                             alt_octet_string_len;
    public CmsInt32                             alt_visible_string_len;
    public CmsInt32                             alt_unicode_string_len;

    public CmsDataDefinition() { super(Codec.DATA_DEFINITION);
        this.choice                 = new CmsEnumerated();
        this.alt_error              = new CmsServiceError();
        this.alt_array              = new CmsDataDefinitionArray();
        this.alt_structure          = new CmsArray<>(CmsDataDefinitionStructElem.class);
        this.alt_structure.allocSize = 0; /* alloc on demand during read, not write */
        this.alt_bit_string_len     = new CmsInt32();
        this.alt_octet_string_len   = new CmsInt32();
        this.alt_visible_string_len = new CmsInt32();
        this.alt_unicode_string_len = new CmsInt32();
    }
    
    public CmsDataDefinition choice(int v) { this.choice.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        // Must match cms_data_definition_t C struct slot order:
        // [0]=choice, [1]=error, [2]=array, [3]=structure, [4]=bit-string, [5]=octet-string, [6]=visible-string, [7]=unicode-string
        return Arrays.asList(choice, alt_error, alt_array, alt_structure,
            alt_bit_string_len, alt_octet_string_len,
            alt_visible_string_len, alt_unicode_string_len);
    }
}