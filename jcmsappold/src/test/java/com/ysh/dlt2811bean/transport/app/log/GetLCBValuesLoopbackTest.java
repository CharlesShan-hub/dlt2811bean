package com.ysh.dlt2811bean.transport.app.log;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetLCBValues;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetLCBValues Loopback Test")
class GetLCBValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("valid LCB reference returns LCB data")
    void validRef() throws Exception {
        associate();

        CmsApdu response = client.getLCBValues("C1/LLN0.Log");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetLCBValues asdu = (CmsGetLCBValues) response.getAsdu();
        assertTrue(asdu.lcb.size() > 0);
        assertEquals(1, asdu.lcb.get(0).getSelectedIndex());
    }

    @Test
    @DisplayName("unknown LCB reference returns error")
    void unknownRef() throws Exception {
        associate();

        CmsApdu response = client.getLCBValues("C1/LLN0.Unknown");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetLCBValues asdu = (CmsGetLCBValues) response.getAsdu();
        assertTrue(asdu.lcb.size() > 0);
        assertEquals(0, asdu.lcb.get(0).getSelectedIndex());
    }
}
