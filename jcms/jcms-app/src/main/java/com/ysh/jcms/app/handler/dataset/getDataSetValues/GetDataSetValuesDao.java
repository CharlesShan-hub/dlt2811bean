package com.ysh.jcms.app.handler.dataset.getDataSetValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetDataSetValuesDao extends BaseDao {
    public GetDataSetValuesDao() {
        paginationContext(new PaginationContext());
    }

    private String datasetReference;
    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        return new CmsGetDataSetValuesRequest().datasetReference(datasetReference).referenceAfter(referenceAfter);
    }
}
