package com.ysh.jcms.pdu.file;

import com.ysh.jcms.data.sequence.common.CmsFileEntry;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;

/**
 * <pre>
 * {@code
 * GetFileAttributeValues-ResponsePDU ::= FileEntry — 8.12.4
 * }
 * </pre>
 *
 * <p>
 * Type alias of FileEntry — same ASN.1 type, same PER encoding. Extends
 * {@link CmsFileEntry}; covariant fluent setters keep the return type when
 * chaining.
 */
public class CmsGetFileAttributeValuesResponse extends CmsFileEntry {

    public CmsGetFileAttributeValuesResponse() {
    }

    @Override
    public CmsGetFileAttributeValuesResponse fileName(String v) {
        super.fileName(v);
        return this;
    }
    @Override
    public CmsGetFileAttributeValuesResponse fileSize(long v) {
        super.fileSize(v);
        return this;
    }
    @Override
    public CmsGetFileAttributeValuesResponse lastModified(CmsUtcTime v) {
        super.lastModified(v);
        return this;
    }
    @Override
    public CmsGetFileAttributeValuesResponse checkSum(long v) {
        super.checkSum(v);
        return this;
    }
    @Override
    public CmsGetFileAttributeValuesResponse value(CmsFileEntry v) {
        super.value(v);
        return this;
    }
}
