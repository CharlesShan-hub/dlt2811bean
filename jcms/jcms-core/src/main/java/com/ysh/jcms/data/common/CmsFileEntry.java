package com.ysh.jcms.data.common;

import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.data.time.CmsUtcTime;
import java.util.Arrays;
import java.util.List;

/**
 * FileEntry ::= SEQUENCE { fileName, fileSize, lastModified, checkSum }  —  7.3.10
 */
public class CmsFileEntry extends CmsType {

    public CmsUint8Array fileName;
    public CmsInt32U     fileSize;
    public CmsUtcTime    lastModified;
    public CmsInt32U     checkSum;

    public CmsFileEntry() {
        this.fileName     = new CmsUint8Array();
        this.fileSize     = new CmsInt32U();
        this.lastModified = new CmsUtcTime();
        this.checkSum     = new CmsInt32U();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(fileName, fileSize, lastModified, checkSum);
    }

    @Override
    public byte[] encode() { write(); return NativeBridge.encodeFileEntry(nativePtr); }
    @Override
    public void decode(byte[] data) { write(); NativeBridge.decodeFileEntry(nativePtr, data); read(); }
}
