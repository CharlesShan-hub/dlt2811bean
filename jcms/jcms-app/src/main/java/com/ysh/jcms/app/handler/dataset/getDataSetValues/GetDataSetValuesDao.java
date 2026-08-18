package com.ysh.jcms.app.handler.dataset.getDataSetValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.dataset.CmsGetDataSetValuesRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetDataSetValuesDao extends BaseDao {

    /** Data set reference, e.g. "LD0/LLN0.dsData" */
    private String datasetReference;

    /** Optional pagination: return items after this reference */
    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(datasetReference, "datasetReference must not be null");
        return new CmsGetDataSetValuesRequest()
            .datasetReference(datasetReference)
            .referenceAfter(referenceAfter);
    }
}
