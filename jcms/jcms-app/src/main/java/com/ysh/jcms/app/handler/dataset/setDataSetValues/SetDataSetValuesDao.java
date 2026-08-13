package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import com.ysh.jcms.core.util.CmsPrinter;
import com.ysh.jcms.app.handler.BaseDao;
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
        if (referenceAfter != null && !referenceAfter.isEmpty())
            req.referenceAfter(referenceAfter);
        if (values != null) {
            CmsPrinter.consoleOnly("[DEBUG] set-dataset-values ds=" + datasetReference + " values=" + values);
            for (String val : values) {
                CmsData data = new CmsData();
                fillCmsData(data, val);
                req.value.add(data);
            }
        }
        return req;
    }

    private static void fillCmsData(CmsData data, String value) {
        if (containsNonAscii(value)) {
            data.alt_unicode_string(value);
        } else {
            data.alt_visible_string(value);
        }
    }

    private static boolean containsNonAscii(String s) {
        if (s == null)
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) > 127)
                return true;
        }
        return false;
    }
}
