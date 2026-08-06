package com.ysh.jcms.app.handler.dataset.deleteDataSet;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.pdu.dataset.CmsDeleteDataSetRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class DeleteDataSetDao extends BaseDao {
    private String datasetReference;

    @Override
    public CmsType toRequest() {
        return new CmsDeleteDataSetRequest().datasetReference(datasetReference);
    }
}
