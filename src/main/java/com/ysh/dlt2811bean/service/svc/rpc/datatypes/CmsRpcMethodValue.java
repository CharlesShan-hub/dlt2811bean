package com.ysh.dlt2811bean.service.svc.rpc.datatypes;

import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsRpcMethodValue extends AbstractCmsCompound<CmsRpcMethodValue> {

    public CmsInt32U timeout = new CmsInt32U();
    public CmsInt32U version = new CmsInt32U();
    public CmsDataDefinition request = new CmsDataDefinition();
    public CmsDataDefinition response = new CmsDataDefinition();

    public CmsRpcMethodValue() {
        super("RpcMethodValue");
        registerField("timeout");
        registerField("version");
        registerField("request");
        registerField("response");
    }

    @Override
    public CmsRpcMethodValue copy() {
        CmsRpcMethodValue copy = new CmsRpcMethodValue();
        copy.timeout = timeout.copy();
        copy.version = version.copy();
        copy.request = request.copy();
        copy.response = response.copy();
        return copy;
    }
}
