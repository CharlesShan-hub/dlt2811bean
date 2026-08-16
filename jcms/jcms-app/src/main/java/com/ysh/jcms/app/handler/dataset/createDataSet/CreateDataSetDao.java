package com.ysh.jcms.app.handler.dataset.createDataSet;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.scalar.CmsFC;
import com.ysh.jcms.core.data.sequence.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.core.pdu.dataset.CmsCreateDataSetRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class CreateDataSetDao extends BaseDao {
    private String datasetReference;
    private String referenceAfter;
    private List<String> memberRefs;
    private List<String> memberFcs;

    @Override
    public CmsType toRequest() {
        CmsCreateDataSetRequest req = new CmsCreateDataSetRequest().datasetReference(datasetReference);
        setIfNotEmpty(req::referenceAfter, referenceAfter);
        if (memberRefs != null && memberFcs != null) {
            int size = Math.min(memberRefs.size(), memberFcs.size());
            for (int i = 0; i < size; i++) {
                String ref = memberRefs.get(i);
                String fcStr = memberFcs.get(i);
                if (ref == null || ref.isEmpty() || fcStr == null || fcStr.isEmpty())
                    continue;
                int fcCode = CmsFC.fromString(fcStr);
                req.memberData.add(new CmsDataRefFcEntry().reference(ref).fc(fcCode));
            }
        }
        return req;
    }
}
