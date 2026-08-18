package com.ysh.jcms.app.handler.data.getDataDirectory;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.data.CmsGetDataDirectoryRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetDataDirectoryDao extends BaseDao {

    /** Data reference, e.g. "LD0/LLN0.Mod.stVal" */
    private String dataReference;

    /** Optional pagination: return items after this reference */
    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(dataReference, "dataReference must not be null");
        return new CmsGetDataDirectoryRequest()
            .dataReference(dataReference)
            .referenceAfter(referenceAfter);
    }
}
