package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.app.util.CmsRequestHelper;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.dataset.CmsGetDataSetDirectoryRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetDataSetDirectoryDao extends BaseDao {
    private String datasetReference;
    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        CmsGetDataSetDirectoryRequest req = new CmsGetDataSetDirectoryRequest().datasetReference(datasetReference);
        CmsRequestHelper.setIfNotEmpty(req::referenceAfter, referenceAfter);
        return req;
    }
}
