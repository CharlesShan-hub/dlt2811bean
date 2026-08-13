package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.app.util.CmsDataFiller;
import com.ysh.jcms.app.util.CmsRequestHelper;
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
    private List<String> values;

    @Override
    public CmsType toRequest() {
        CmsSetDataSetValuesRequest req = new CmsSetDataSetValuesRequest().datasetReference(datasetReference);
        CmsRequestHelper.setIfNotEmpty(req::referenceAfter, referenceAfter);
        if (values != null) {
            for (String val : values) {
                CmsData data = new CmsData();
                CmsDataFiller.fillCmsData(data, val);
                req.value.add(data);
            }
        }
        return req;
    }
}