package com.ysh.dlt2811bean.transport.app.setting;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetEditSGValue;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetEditSGValue Loopback Test")
class GetEditSGValueLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("get edit SG value returns Response+")
    void validRef() throws Exception {
        associate();

        CmsApdu response = client.getEditSGValue("C1/LPHD1.Proxy.stVal", "SE");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetEditSGValue asdu = (CmsGetEditSGValue) response.getAsdu();
        assertNotNull(asdu.value());
    }
}
