package com.ysh.jcms.app.handler.rpc.getRpcMethodDirectory;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcMethodDirectoryRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetRpcMethodDirectoryDao extends BaseDao {

    /** Interface name to query (optional, omit to get all interfaces' methods) */
    private String iface;

    /** Optional pagination: return methods after this reference */
    private String after;

    @Override
    public CmsType toRequest() {
        return new CmsGetRpcMethodDirectoryRequest()
            .interfaceName(iface)
            .referenceAfter(after);
    }
}
