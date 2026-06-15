package com.ysh.dlt2811bean.transport.app.log;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetLogStatusValues;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetLogStatusValues Loopback Test")
class GetLogStatusValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("valid log reference returns Response+ with status value")
    void validRef() throws Exception {
        associate();

        CmsApdu response = client.getLogStatusValues("C1/LLN0.Log");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetLogStatusValues asdu = (CmsGetLogStatusValues) response.getAsdu();
        assertTrue(asdu.log().size() > 0);
        assertEquals(1, asdu.log().get(0).getSelectedIndex());
    }

    @Test
    @DisplayName("unknown log reference returns error")
    void unknownRef() throws Exception {
        associate();

        CmsApdu response = client.getLogStatusValues("C1/LLN0.Unknown");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetLogStatusValues asdu = (CmsGetLogStatusValues) response.getAsdu();
        assertEquals(0, asdu.log().get(0).getSelectedIndex());
    }
}
