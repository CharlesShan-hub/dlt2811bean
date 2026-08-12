package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.data.InnerGetLogicalNodeDirectoryResponsePDU;
import com.ysh.jcms.core.data.scalar.CmsSubReference;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetLogicalNodeDirectory-ResponsePDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT SEQUENCE OF SubReference,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.3.3
 * }
 * </pre>
 */
public class CmsGetLogicalNodeDirectoryResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSubReference.class)
    public List<CmsSubReference> reference; /* SEQUENCE OF SubReference */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetLogicalNodeDirectoryResponse() {
        super(new InnerGetLogicalNodeDirectoryResponsePDU());
        this.reference = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetLogicalNodeDirectoryResponse reference(List<CmsSubReference> v) {
        this.reference = v;
        return this;
    }
    public CmsGetLogicalNodeDirectoryResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    /** Convenience: extract reference strings as List. */
    public List<String> refs() {
        List<String> refs = new ArrayList<>();
        for (CmsSubReference ref : reference) {
            refs.add(ref.value());
        }
        return refs;
    }
}
