package com.ysh.jcms.pdu.file;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerDeleteFileRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * DeleteFile-RequestPDU ::= SEQUENCE { filename [0] IMPLICIT VisibleString
 * (SIZE (0..255)) } — 8.12.3
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
