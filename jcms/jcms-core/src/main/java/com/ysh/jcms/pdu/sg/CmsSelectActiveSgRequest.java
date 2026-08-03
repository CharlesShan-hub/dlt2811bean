package com.ysh.jcms.pdu.sg;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerSelectActiveSGRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * SelectActiveSG-RequestPDU ::= SEQUENCE {
 *     sgcbReference       [0] IMPLICIT ObjectReference,
 *     settingGroupNumber  [1] IMPLICIT INT8U
 * } — 8.6.1
 * }
 * </pre>
 */
public class CmsSelectActiveSgRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference sgcbReference;

    @CmsField
    public CmsInt8U settingGroupNumber;

    public CmsSelectActiveSgRequest() {
        super(new InnerSelectActiveSGRequestPDU());
    }

    public CmsSelectActiveSgRequest sgcbReference(String v) {
        this.sgcbReference.value(v);
        return this;
    }
    public CmsSelectActiveSgRequest sgcbReference(byte[] v) {
        return sgcbReference(new String(v, StandardCharsets.UTF_8));
    }
    public CmsSelectActiveSgRequest settingGroupNumber(int v) {
        this.settingGroupNumber.value(v);
        return this;
    }
}
