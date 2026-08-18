package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.directory.CmsGetLogicalNodeDirectoryRequest;
import com.ysh.jcms.app.handler.base.BaseDao;
import java.util.Objects;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(fluent = true)
public class LnDirDao extends BaseDao {

    /** LD name (e.g. "C1") or LN reference (e.g. "C1/LLN0") — auto-detected by PDU */
    private String reference;

    /** Optional pagination: return items after this reference */
    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(reference, "reference must not be null");
        return new CmsGetLogicalNodeDirectoryRequest()
            .reference(reference)
            .referenceAfter(referenceAfter);
    }
}
