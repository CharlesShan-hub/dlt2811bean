package com.ysh.jcms.app.handler.dataset.getDataSetValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.app.util.CmsRequestHelper;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.dataset.CmsGetDataSetValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetDataSetValuesDao extends BaseDao {
    private String datasetReference;
    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        CmsGetDataSetValuesRequest req = new CmsGetDataSetValuesRequest().datasetReference(datasetReference);
        CmsRequestHelper.setIfNotEmpty(req::referenceAfter, referenceAfter);
        return req;
    }
}
