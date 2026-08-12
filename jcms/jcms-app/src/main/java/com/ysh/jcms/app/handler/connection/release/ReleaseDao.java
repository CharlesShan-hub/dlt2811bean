package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.connection.CmsReleaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class ReleaseDao extends BaseDao {
    private byte[] associationId;

    @Override
    public CmsType toRequest() {
        CmsReleaseRequest req = new CmsReleaseRequest();
        if (associationId != null && associationId.length > 0) {
            req.associationId(associationId);
        }
        return req;
    }
}
