package com.ysh.jcms.pdu.file;

import com.ysh.jcms.data.InnerSetFileRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.scalar.CmsOctetString;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * SetFile-RequestPDU ::= SEQUENCE {
 *     filename        [0] IMPLICIT VisibleString (SIZE (0..255)),
 *     startPosition   [1] IMPLICIT Int32U,
 *     fileData        [2] IMPLICIT OCTET STRING,
 *     endOfFile       [3] IMPLICIT Boolean DEFAULT 0
 * } — 8.12.2
 */
public class CmsSetFileRequest extends CmsSequence {

    @CmsField
    public CmsString filename;

    @CmsField
    public CmsInt32U startPosition;

    @CmsField
    public CmsOctetString fileData;

    @CmsField
    public CmsBoolean endOfFile; /* DEFAULT FALSE */

    public CmsSetFileRequest() {
        super(new InnerSetFileRequestPDU());
        this.endOfFile.value(false);
    }

    public CmsSetFileRequest filename(String v) { this.filename.value(v); return this; }
    public CmsSetFileRequest filename(byte[] v) { return filename(new String(v)); }
    public CmsSetFileRequest startPosition(long v) { this.startPosition.value(v); return this; }
    public CmsSetFileRequest fileData(byte[] v) { this.fileData.value(v); return this; }
    public CmsSetFileRequest endOfFile(boolean v) { this.endOfFile.value(v); return this; }
}
