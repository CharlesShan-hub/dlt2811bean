package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.dataset.CmsSetDataSetValuesRequest;
import java.util.Objects;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetDataSetValuesDao extends BaseDao {

    /** Data set reference, e.g. "LD0/LLN0.dsAlarm" */
    private String datasetReference;

    /** Optional pagination: return items after this reference */
    private String referenceAfter;

    /** Values to set, in dataset member order */
    private List<CmsData> values;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(datasetReference, "datasetReference must not be null");
        Objects.requireNonNull(values, "values must not be null");

        return new CmsSetDataSetValuesRequest()
            .datasetReference(datasetReference)
            .referenceAfter(referenceAfter)
            .value(values);
    }
}
