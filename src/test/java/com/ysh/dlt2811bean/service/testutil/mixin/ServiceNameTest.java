package com.ysh.dlt2811bean.service.testutil.mixin;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface ServiceNameTest<T extends CmsAsdu<T>> {

    @Test
    default void serviceName() {
        assertEquals(expectedServiceName(), createAsdu().getServiceName());
    }

    ServiceName expectedServiceName();

    T createAsdu();
}
