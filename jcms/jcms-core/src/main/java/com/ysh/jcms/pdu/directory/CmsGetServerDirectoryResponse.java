package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerGetServerDirectoryResponsePDU;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetServerDirectory-ResponsePDU ::= SEQUENCE {
 *     reference        [0] IMPLICIT SEQUENCE OF ObjectReference,
 *     moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.3.1
 * }
 * </pre>
 */
public class CmsGetServerDirectoryResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsObjectReference.class)
    public List<CmsObjectReference> reference; /* SEQUENCE OF ObjectReference */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetServerDirectoryResponse() {
        super(new InnerGetServerDirectoryResponsePDU());
        this.reference = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetServerDirectoryResponse reference(List<CmsObjectReference> v) {
        this.reference = v;
        return this;
    }
    public CmsGetServerDirectoryResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    /** Convenience: extract LD names as String list. */
    public List<String> ldNames() {
        List<String> names = new ArrayList<>();
        for (CmsObjectReference ref : reference) {
            names.add(ref.value());
        }
        return names;
    }
}
