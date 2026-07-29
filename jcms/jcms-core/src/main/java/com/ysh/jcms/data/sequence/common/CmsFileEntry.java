package com.ysh.jcms.data.sequence.common;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerFileEntry;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.core.CmsString;

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
    public CmsFileEntry lastModified(CmsUtcTime v) { this.lastModified.value(v); return this; }
    public CmsFileEntry checkSum(long v) { this.checkSum.value(v); return this; }

    /** Copy all field values from another CmsFileEntry (fluent). */
    public CmsFileEntry value(CmsFileEntry v) {
        return fileName(v.fileName.value())
            .fileSize(v.fileSize.value())
            .lastModified(v.lastModified)
            .checkSum(v.checkSum.value());
    }
}
