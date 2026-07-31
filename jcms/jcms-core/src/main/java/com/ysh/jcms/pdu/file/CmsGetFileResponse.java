package com.ysh.jcms.pdu.file;

import com.ysh.jcms.data.InnerGetFileResponsePDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsOctetString;

/**
 * GetFile-ResponsePDU ::= SEQUENCE {
 *     fileData    [0] IMPLICIT OCTET STRING,
 *     endOfFile   [1] IMPLICIT Boolean DEFAULT 0
 * } — 8.12.1
 */
public class CmsGetFileResponse extends CmsSequence {

    @CmsField
    public CmsOctetString fileData;

    @CmsField
    public CmsBoolean endOfFile; /* DEFAULT FALSE */

    public CmsGetFileResponse() {
        super(new InnerGetFileResponsePDU());
        this.endOfFile.value(false);
    }

    public CmsGetFileResponse fileData(byte[] v) { this.fileData.value(v); return this; }
    public CmsGetFileResponse endOfFile(boolean v) { this.endOfFile.value(v); return this; }
}
