package com.ysh.jcms.pdu.file;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerGetFileAttributeValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * <pre>
 * {@code
 * GetFileAttributeValues-RequestPDU ::= SEQUENCE {
 *     filename        [0] IMPLICIT VisibleString255
 * } — 8.12.4
 * }
 * </pre>
 */
public class CmsGetFileAttributeValuesRequest extends CmsSequence {

    @CmsField
    public CmsString filename;

    public CmsGetFileAttributeValuesRequest() {
        super(new InnerGetFileAttributeValuesRequestPDU());
    }

    public CmsGetFileAttributeValuesRequest filename(String v) {
        this.filename.value(v);
        return this;
    }
    public CmsGetFileAttributeValuesRequest filename(byte[] v) {
        return filename(new String(v, StandardCharsets.UTF_8));
    }
}
