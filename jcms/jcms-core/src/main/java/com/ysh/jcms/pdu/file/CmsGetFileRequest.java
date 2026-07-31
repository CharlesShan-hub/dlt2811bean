package com.ysh.jcms.pdu.file;

import com.ysh.jcms.data.InnerGetFileRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * GetFile-RequestPDU ::= SEQUENCE {
 *     filename        [0] IMPLICIT VisibleString (SIZE (0..255)),
 *     startPosition   [1] IMPLICIT Int32U
 * } — 8.12.1
 */
public class CmsGetFileRequest extends CmsSequence {

    @CmsField
    public CmsString filename;

    @CmsField
    public CmsInt32U startPosition;

    public CmsGetFileRequest() {
        super(new InnerGetFileRequestPDU());
    }

    public CmsGetFileRequest filename(String v) { this.filename.value(v); return this; }
    public CmsGetFileRequest filename(byte[] v) { return filename(new String(v)); }
    public CmsGetFileRequest startPosition(long v) { this.startPosition.value(v); return this; }
}
