package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.data.InnerGetLogicalDeviceDirectoryResponsePDU;
import com.ysh.jcms.core.data.scalar.CmsSubReference;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetLogicalDeviceDirectory-ResponsePDU ::= SEQUENCE {
 *     lnReference       [0] IMPLICIT SEQUENCE OF SubReference,
 *     moreFollows       [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.3.2
 * }
 * </pre>
 */
public class CmsGetLogicalDeviceDirectoryResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSubReference.class)
    public List<CmsSubReference> lnReference; /* SEQUENCE OF SubReference */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetLogicalDeviceDirectoryResponse() {
        super(new InnerGetLogicalDeviceDirectoryResponsePDU());
        this.lnReference = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetLogicalDeviceDirectoryResponse lnReference(List<CmsSubReference> v) {
        this.lnReference = v;
        return this;
    }
    public CmsGetLogicalDeviceDirectoryResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    /** Convenience: extract LN reference strings as List. */
    public List<String> lnNames() {
        List<String> names = new ArrayList<>();
        for (CmsSubReference ref : lnReference) {
            names.add(ref.value());
        }
        return names;
    }
}
