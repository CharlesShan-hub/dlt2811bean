package com.ysh.jcms2.data.common;

import com.ysh.jcms2.core.CmsType;
import com.ysh.jcms2.data.scalar.CmsInt32U;
import com.ysh.jcms2.data.string.CmsUint8Array;
import com.ysh.jcms2.data.time.CmsUtcTime;
import java.util.Arrays;
import java.util.List;

/**
 * FileEntry ::= SEQUENCE {
 *     fileName     [0] VisibleString129,
 *     fileSize     [1] INT32U,
 *     lastModified [2] UtcTime,
 *     checkSum     [3] INT32U
 * }  —  7.3.10
 *
 * All-pointer container:
 *   [0] fileName      → CmsUint8Array*
 *   [8] fileSize      → CmsInt32U*
 *   [16] lastModified → CmsUtcTime*
 *   [24] checkSum     → CmsInt32U*
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
}
