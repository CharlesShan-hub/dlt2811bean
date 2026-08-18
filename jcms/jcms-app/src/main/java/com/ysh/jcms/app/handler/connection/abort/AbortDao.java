package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.connection.CmsAbort;
import java.util.Objects;
import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class AbortDao extends BaseDao {
    private Integer reason;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(reason, "reason must not be null");
        return new CmsAbort().reason(reason);
    }
}
