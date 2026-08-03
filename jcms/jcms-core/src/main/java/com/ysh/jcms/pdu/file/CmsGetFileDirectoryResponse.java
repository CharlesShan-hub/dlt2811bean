package com.ysh.jcms.pdu.file;

import com.ysh.jcms.data.InnerGetFileDirectoryResponsePDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.sequence.common.CmsFileEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * GetFileDirectory-ResponsePDU ::= SEQUENCE { fileEntry [0] IMPLICIT SEQUENCE
 * OF FileEntry, moreFollows [1] IMPLICIT Boolean DEFAULT 1 } — 8.12.5
 */
public class CmsGetFileDirectoryResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsFileEntry.class)
    public List<CmsFileEntry> fileEntry; /* SEQUENCE OF FileEntry */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetFileDirectoryResponse() {
        super(new InnerGetFileDirectoryResponsePDU());
        this.fileEntry = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetFileDirectoryResponse fileEntry(List<CmsFileEntry> v) {
        this.fileEntry = v;
        return this;
    }
    public CmsGetFileDirectoryResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }
}
