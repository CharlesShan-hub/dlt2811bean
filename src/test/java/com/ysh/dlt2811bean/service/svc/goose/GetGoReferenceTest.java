package com.ysh.dlt2811bean.service.svc.goose;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetGoReferenceTest implements ServiceNameTest<com.ysh.dlt2811bean.service.svc.goose.CmsGetGoReference> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.GET_GO_REFERENCE;    
    }

    @Override
    public com.ysh.dlt2811bean.service.svc.goose.CmsGetGoReference createAsdu() {
        return new com.ysh.dlt2811bean.service.svc.goose.CmsGetGoReference(MessageType.REQUEST);
    }
}
