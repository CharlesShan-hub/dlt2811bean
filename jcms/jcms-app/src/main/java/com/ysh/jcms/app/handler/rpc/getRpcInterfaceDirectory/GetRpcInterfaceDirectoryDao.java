package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDirectory;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcInterfaceDirectoryRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetRpcInterfaceDirectoryDao extends BaseDao {
    private String after;

    @Override
    public CmsType toRequest() {
        CmsGetRpcInterfaceDirectoryRequest req = new CmsGetRpcInterfaceDirectoryRequest();
        if (after != null && !after.isEmpty())
            req.referenceAfter(after);
        return req;
    }
}
