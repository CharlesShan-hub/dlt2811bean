package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesRequest;
import lombok.Getter;
import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.app.handler.PaginationContext;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class AllDataValuesDao extends BaseDao {

    public AllDataValuesDao() {
        paginationContext(new PaginationContext());
    }

    /** ldName (e.g. "LD0") — alternative to lnReference */
    private String ldName;
    /** lnReference (e.g. "LD0/LLN0") — alternative to ldName */
    private String lnReference;
    /** Optional FunctionalConstraint filter */
    private Integer fc;
    /** Optional pagination: return items after this reference */
    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        CmsGetAllDataValuesRequest req = new CmsGetAllDataValuesRequest().referenceAfter(referenceAfter);
        if (ldName != null) {
            req.reference.altLdName(ldName);
        } else if (lnReference != null) {
            req.reference.altLnReference(lnReference);
        }
        if (fc != null) {
            req.fc(fc);
        }
        return req;
    }
}
