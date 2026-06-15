package com.ysh.dlt2811bean.service.svc.rpc.datatypes;

import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsRpcMethodDefEntry extends AbstractCmsCompound<CmsRpcMethodDefEntry> {

    public CmsVisibleString name = new CmsVisibleString().max(255);
    public CmsInt32U version = new CmsInt32U();
    public CmsInt32U timeout = new CmsInt32U();
    public CmsDataDefinition request = new CmsDataDefinition();
    public CmsDataDefinition response = new CmsDataDefinition();

    public CmsRpcMethodDefEntry() {
        super("RpcMethodDefEntry");
        registerField("name");
        registerField("version");
        registerField("timeout");
        registerField("request");
        registerField("response");
    }

    @Override
    public CmsRpcMethodDefEntry copy() {
        CmsRpcMethodDefEntry copy = new CmsRpcMethodDefEntry();
        copy.name = name.copy();
        copy.version = version.copy();
        copy.timeout = timeout.copy();
        copy.request = request.copy();
        copy.response = response.copy();
        return copy;
    }
}
