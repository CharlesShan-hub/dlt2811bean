package com.ysh.jcms.app.handler.rpc.getRpcMethodDirectory;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.pdu.rpc.CmsGetRpcMethodDirectoryRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetRpcMethodDirectoryDao extends BaseDao {
    private String iface;
    private String after;

    @Override
    public CmsType toRequest() {
        CmsGetRpcMethodDirectoryRequest req = new CmsGetRpcMethodDirectoryRequest();
        if (iface != null && !iface.isEmpty())
            req.interfaceName(iface);
        if (after != null && !after.isEmpty())
            req.referenceAfter(after);
        return req;
    }
}
