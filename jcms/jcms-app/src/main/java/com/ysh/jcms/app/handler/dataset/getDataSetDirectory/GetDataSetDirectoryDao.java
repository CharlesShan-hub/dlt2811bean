package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import com.ysh.jcms.app.handler.BaseDao;
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
        return new CmsGetDataSetDirectoryRequest().datasetReference(datasetReference).referenceAfter(referenceAfter);
    }
}
