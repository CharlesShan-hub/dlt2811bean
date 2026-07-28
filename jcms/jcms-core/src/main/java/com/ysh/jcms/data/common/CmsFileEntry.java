package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerFileEntry;
import com.ysh.jcms.data.InnerUtcTime;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.time.CmsUtcTime;

/**
 * FileEntry ::= SEQUENCE { fileName, fileSize, lastModified, checkSum } —
 * 7.3.10
 * <p>
 * Wraps {@link InnerFileEntry} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsFileEntry extends CmsType {

    public String fileName;
    public CmsInt32U fileSize;
    public CmsUtcTime lastModified;
    public CmsInt32U checkSum;

    public CmsFileEntry() {
        super(new InnerFileEntry());
        this.fileName = "";
        this.fileSize = new CmsInt32U();
        this.lastModified = new CmsUtcTime();
        this.checkSum = new CmsInt32U();
    }

    public CmsFileEntry fileName(String v) { this.fileName = v; return this; }
    public CmsFileEntry fileSize(long v) { this.fileSize.value(v); return this; }
    public CmsFileEntry lastModified(CmsUtcTime v) { this.lastModified = v; return this; }
    public CmsFileEntry checkSum(long v) { this.checkSum.value(v); return this; }

    @Override
    public void syncToInner() {
        InnerFileEntry i = (InnerFileEntry) inner;
        i.fileName.value = fileName;
        i.fileSize.value = (int) fileSize.value();
        lastModified.syncToInner();
        i.lastModified.value = ((InnerUtcTime) lastModified.inner).value;
        i.checkSum.value = (int) checkSum.value();
    }

    @Override
    public void syncFromInner() {
        InnerFileEntry i = (InnerFileEntry) inner;
        fileName = i.fileName.value;
        fileSize.value(i.fileSize.value & 0xFFFFFFFFL);
        ((InnerUtcTime) lastModified.inner).value = i.lastModified.value;
        lastModified.syncFromInner();
        checkSum.value(i.checkSum.value & 0xFFFFFFFFL);
    }
}
