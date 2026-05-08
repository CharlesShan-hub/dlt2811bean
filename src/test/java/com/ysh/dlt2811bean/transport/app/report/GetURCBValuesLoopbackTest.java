package com.ysh.dlt2811bean.transport.app.report;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsGetURCBValues;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetURCBValues Loopback Test")
class GetURCBValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("valid URCB reference returns URCB data")
    void validRef() throws Exception {
        associate();

        CmsApdu response = client.getURCBValues("C1/LLN0.PosReport");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetURCBValues asdu = (CmsGetURCBValues) response.getAsdu();
        assertTrue(asdu.urcb.size() > 0);
        assertEquals(1, asdu.urcb.get(0).getSelectedIndex());
    }

    @Test
    @DisplayName("unknown URCB reference returns error")
    void unknownRef() throws Exception {
        associate();

        CmsApdu response = client.getURCBValues("C1/LLN0.Unknown");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetURCBValues asdu = (CmsGetURCBValues) response.getAsdu();
        assertTrue(asdu.urcb.size() > 0);
        assertEquals(0, asdu.urcb.get(0).getSelectedIndex());
    }

    @Test
    @DisplayName("empty reference returns error")
    void emptyRef() throws Exception {
        associate();

        CmsApdu response = client.getURCBValues("");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetURCBValues asdu = (CmsGetURCBValues) response.getAsdu();
        assertTrue(asdu.urcb.size() > 0);
        assertEquals(0, asdu.urcb.get(0).getSelectedIndex());
    }
}
