package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDefinition;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcInterfaceDefinitionRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetRpcInterfaceDefinitionDao extends BaseDao {

    /** Interface name to query */
    private String iface;

    /** Optional pagination: return methods after this reference */
    private String after;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(iface, "iface must not be null");
        return new CmsGetRpcInterfaceDefinitionRequest()
            .interfaceName(iface)
            .referenceAfter(after);
    }
}
