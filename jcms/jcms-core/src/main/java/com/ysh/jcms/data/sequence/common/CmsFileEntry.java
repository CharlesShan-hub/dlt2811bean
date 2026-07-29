package com.ysh.jcms.data.sequence.common;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerFileEntry;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.core.CmsString;
import com.ysh.jcms.data.sequence.time.CmsUtcTime;

/**
 * FileEntry ::= SEQUENCE { fileName, fileSize, lastModified, checkSum } —
 * 7.3.10
 * <p>
 * Wraps {@link InnerFileEntry} for PER encode/decode via Rust.
 */
public class CmsFileEntry extends CmsSequence {

    @CmsField public CmsString fileName;
    @CmsField public CmsInt32U fileSize;
    @CmsField public CmsUtcTime lastModified;
    @CmsField public CmsInt32U checkSum;

    public CmsFileEntry() {
        super(new InnerFileEntry());
    }

    public CmsFileEntry fileName(String v) { this.fileName.value(v); return this; }
    public CmsFileEntry fileSize(long v) { this.fileSize.value(v); return this; }
    public CmsFileEntry lastModified(CmsUtcTime v) {
        this.lastModified = v;
        bindWrapper("lastModified", v);
        return this;
    }
    public CmsFileEntry checkSum(long v) { this.checkSum.value(v); return this; }
}
