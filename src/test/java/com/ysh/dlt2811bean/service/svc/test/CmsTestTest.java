package com.ysh.dlt2811bean.service.svc.test;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTest")
class CmsTestTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsTest asdu = new CmsTest();

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsTest result = (CmsTest) decoded.getAsdu();
        assertEquals(ServiceName.TEST, result.getServiceName());

        System.out.println(result);
    }
}