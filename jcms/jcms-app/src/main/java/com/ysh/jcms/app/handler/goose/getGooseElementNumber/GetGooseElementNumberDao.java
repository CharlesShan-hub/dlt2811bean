package com.ysh.jcms.app.handler.goose.getGooseElementNumber;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.goose.CmsGoRefFcEntry;
import com.ysh.jcms.core.pdu.goose.CmsGetGooseElementNumberRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetGooseElementNumberDao extends BaseDao {
    private String gocbReference;
    private List<String> memberRefs;
    private List<String> memberFcs;

    @Override
    public CmsType toRequest() {
        CmsGetGooseElementNumberRequest req = new CmsGetGooseElementNumberRequest().gocbReference(gocbReference);
        if (memberRefs != null && memberFcs != null) {
            int len = Math.min(memberRefs.size(), memberFcs.size());
            for (int i = 0; i < len; i++) {
                req.memberData.add(new CmsGoRefFcEntry().reference(memberRefs.get(i)).fc(Integer.parseInt(memberFcs.get(i))));
            }
        }
        return req;
    }
}
