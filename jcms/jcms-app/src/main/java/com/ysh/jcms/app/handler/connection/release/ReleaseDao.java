package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.connection.CmsReleaseRequest;
import java.util.Objects;
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
        Objects.requireNonNull(associationId, "associationId must not be null");
        return new CmsReleaseRequest()
            .associationId(associationId);
    }
}
