package com.ysh.jcms.pdu.file;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerDeleteFileRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * <pre>
 * {@code
 * DeleteFile-RequestPDU ::= SEQUENCE {
 *     filename        [0] IMPLICIT VisibleString255
 * } — 8.12.3
 * }
 * </pre>
 */
public class CmsDeleteFileRequest extends CmsSequence {

    @CmsField
    public CmsString filename;

    public CmsDeleteFileRequest() {
        super(new InnerDeleteFileRequestPDU());
    }

    public CmsDeleteFileRequest filename(String v) {
        this.filename.value(v);
        return this;
    }
    public CmsDeleteFileRequest filename(byte[] v) {
        return filename(new String(v, StandardCharsets.UTF_8));
    }
}
