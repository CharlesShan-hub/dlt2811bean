package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.pdu.directory.CmsGetLogicalNodeDirectoryRequest;
import lombok.Getter;
import com.ysh.jcms.app.handler.BaseDao;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class LnDirDao extends BaseDao {

    public LnDirDao() {
        paginationContext(new LnDirContext());
    }

    /** ldName (e.g. "C1") — alternative to lnReference */
    private String ldName;
    /** lnReference (e.g. "C1/LLN0") — alternative to ldName */
    private String lnReference;
    /** ACSI class to query, default DATA_OBJECT(1) */
    private int acsiClass = 1;
    /** Optional pagination: return items after this reference */
    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        CmsGetLogicalNodeDirectoryRequest req = new CmsGetLogicalNodeDirectoryRequest().acsiClass(acsiClass).referenceAfter(referenceAfter);
        if (ldName != null) {
            req.reference.altLdName(ldName);
        } else if (lnReference != null) {
            req.reference.altLnReference(lnReference);
        }
        return req;
    }
}
