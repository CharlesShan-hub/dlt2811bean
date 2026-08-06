package com.ysh.jcms.app.handler.dataset.createDataSet;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.pdu.dataset.CmsCreateDataSetRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class CreateDataSetDao extends BaseDao {
    private String datasetReference;
    private String referenceAfter;
    private List<MemberRef> members = new ArrayList<>();

    @Setter
    @Getter
    @Accessors(fluent = true)
    public static class MemberRef {
        private String reference;
        private int fc;
    }

    public CreateDataSetDao addMember(String reference, int fc) {
        members.add(new MemberRef().reference(reference).fc(fc));
        return this;
    }

    @Override
    public CmsType toRequest() {
        CmsCreateDataSetRequest req = new CmsCreateDataSetRequest().datasetReference(datasetReference).referenceAfter(referenceAfter);
        for (MemberRef m : members) {
            req.memberData.add(new CmsDataRefFcEntry().reference(m.reference()).fc(m.fc()));
        }
        return req;
    }
}
