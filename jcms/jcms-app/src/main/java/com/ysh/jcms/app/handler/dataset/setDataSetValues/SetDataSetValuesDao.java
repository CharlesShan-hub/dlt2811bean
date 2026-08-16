package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.dataset.CmsSetDataSetValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class SetDataSetValuesDao extends BaseDao {
    private String datasetReference;
    private String referenceAfter;
    private List<CmsData> values;

    @Override
    public CmsType toRequest() {
        CmsSetDataSetValuesRequest req = new CmsSetDataSetValuesRequest().datasetReference(datasetReference);
        setIfNotEmpty(req::referenceAfter, referenceAfter);
        if (values != null) {
            for (CmsData val : values) {
                req.value.add(val);
            }
        }
        return req;
    }
}
