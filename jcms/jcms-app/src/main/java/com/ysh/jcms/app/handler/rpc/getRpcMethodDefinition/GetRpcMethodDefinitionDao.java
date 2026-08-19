package com.ysh.jcms.app.handler.rpc.getRpcMethodDefinition;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.scalar.CmsString;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcMethodDefinitionRequest;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetRpcMethodDefinitionDao extends BaseDao {

    /** Method references, format "iface.method" */
    private List<String> refs;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(refs, "refs must not be null");
        return new CmsGetRpcMethodDefinitionRequest()
            .reference(
                refs.stream()
                    .map(CmsString::new)
                    .collect(Collectors.toList())
            );
    }
}
