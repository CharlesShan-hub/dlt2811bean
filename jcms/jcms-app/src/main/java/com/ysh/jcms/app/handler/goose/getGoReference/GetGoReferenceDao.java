package com.ysh.jcms.app.handler.goose.getGoReference;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.scalar.CmsInt16U;
import com.ysh.jcms.core.pdu.goose.CmsGetGoReferenceRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetGoReferenceDao extends BaseDao {
    private String gocbReference;
    private List<String> memberOffsets;

    @Override
    public CmsType toRequest() {
        CmsGetGoReferenceRequest req = new CmsGetGoReferenceRequest().gocbReference(gocbReference);
        if (memberOffsets != null) {
            for (String s : memberOffsets) {
                req.memberOfs.add(new CmsInt16U(Integer.parseInt(s)));
            }
        }
        return req;
    }
}
