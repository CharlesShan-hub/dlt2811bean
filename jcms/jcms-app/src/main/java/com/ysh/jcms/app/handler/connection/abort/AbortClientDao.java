package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.pdu.connection.CmsAbort;
import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class AbortClientDao extends BaseDao {
    private int reason;

    @Override
    public CmsType toRequest() {
        return new CmsAbort().reason(reason);
    }
}
