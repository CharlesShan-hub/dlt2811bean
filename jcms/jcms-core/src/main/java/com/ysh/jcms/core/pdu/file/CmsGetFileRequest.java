package com.ysh.jcms.core.pdu.file;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerGetFileRequestPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsInt32U;
import com.ysh.jcms.core.data.scalar.CmsString;

/**
 * <pre>
 * {@code
 * GetFile-RequestPDU ::= SEQUENCE {
 *     filename        [0] IMPLICIT VisibleString255,
 *     startPosition   [1] IMPLICIT INT32U
 * } — 8.12.1
 * }
 * </pre>
 */
public class CmsGetFileRequest extends CmsSequence {

    @CmsField
    public CmsString filename;

    @CmsField
    public CmsInt32U startPosition;

    public CmsGetFileRequest() {
        super(new InnerGetFileRequestPDU());
    }

    public CmsGetFileRequest filename(String v) {
        this.filename.value(v);
        return this;
    }
    public CmsGetFileRequest filename(byte[] v) {
        return filename(new String(v, StandardCharsets.UTF_8));
    }
    public CmsGetFileRequest startPosition(long v) {
        this.startPosition.value(v);
        return this;
    }
}
