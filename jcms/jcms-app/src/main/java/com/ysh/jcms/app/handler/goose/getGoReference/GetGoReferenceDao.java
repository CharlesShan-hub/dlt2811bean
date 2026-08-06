package com.ysh.jcms.app.handler.goose.getGoReference;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.pdu.goose.CmsGetGoReferenceRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetGoReferenceDao extends BaseDao {
    private String gocbReference;
    private final List<Integer> memberOffsets = new ArrayList<>();

    public GetGoReferenceDao addMemberOffset(int offset) {
        memberOffsets.add(offset);
        return this;
    }

    @Override
    public CmsType toRequest() {
        CmsGetGoReferenceRequest req = new CmsGetGoReferenceRequest().gocbReference(gocbReference);
        for (int offset : memberOffsets) {
            req.memberOfs.add(new CmsInt16U(offset));
        }
        return req;
    }
}
