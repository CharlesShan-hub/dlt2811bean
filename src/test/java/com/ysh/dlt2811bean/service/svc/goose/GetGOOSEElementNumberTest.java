package com.ysh.dlt2811bean.service.svc.goose;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;

class GetGooseElementNumberTest implements ServiceNameTest<com.ysh.dlt2811bean.service.svc.goose.CmsGetGooseElementNumber> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.Get_GOOSE_ElementNumber;
    }

    @Override
    public com.ysh.dlt2811bean.service.svc.goose.CmsGetGooseElementNumber createAsdu() {
        return new com.ysh.dlt2811bean.service.svc.goose.CmsGetGooseElementNumber(MessageType.REQUEST);
    }
}
