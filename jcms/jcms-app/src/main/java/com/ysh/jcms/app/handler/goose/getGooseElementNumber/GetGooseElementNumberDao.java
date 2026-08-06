package com.ysh.jcms.app.handler.goose.getGooseElementNumber;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.goose.CmsGoRefFcEntry;
import com.ysh.jcms.pdu.goose.CmsGetGooseElementNumberRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetGooseElementNumberDao extends BaseDao {
    private String gocbReference;
    private final List<MemberSpec> members = new ArrayList<>();

    @Setter
    @Getter
    @Accessors(fluent = true)
    public static class MemberSpec {
        private String reference;
        private int fc;
    }

    public GetGooseElementNumberDao addMember(String reference, int fc) {
        members.add(new MemberSpec().reference(reference).fc(fc));
        return this;
    }

    @Override
    public CmsType toRequest() {
        CmsGetGooseElementNumberRequest req = new CmsGetGooseElementNumberRequest().gocbReference(gocbReference);
        for (MemberSpec spec : members) {
            req.memberData.add(new CmsGoRefFcEntry().reference(spec.reference()).fc(spec.fc()));
        }
        return req;
    }
}
