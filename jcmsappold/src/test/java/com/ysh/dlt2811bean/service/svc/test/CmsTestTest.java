package com.ysh.dlt2811bean.service.svc.test;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTest")
class CmsTestTest implements ServiceNameTest<CmsTest> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.TEST;
    }

    @Override
    public CmsTest createAsdu() {
        return new CmsTest();
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsTest result = AsduTestUtil.roundTripViaApdu(new CmsTest());
        assertEquals(ServiceName.TEST, result.getServiceName());
    }
}
