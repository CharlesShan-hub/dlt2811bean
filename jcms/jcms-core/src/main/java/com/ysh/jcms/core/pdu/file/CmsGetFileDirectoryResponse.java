package com.ysh.jcms.core.pdu.file;

import com.ysh.jcms.data.InnerGetFileDirectoryResponsePDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import com.ysh.jcms.core.data.sequence.common.CmsFileEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetFileDirectory-ResponsePDU ::= SEQUENCE {
 *     fileEntry       [0] IMPLICIT SEQUENCE OF FileEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.12.5
 * }
 * </pre>
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
