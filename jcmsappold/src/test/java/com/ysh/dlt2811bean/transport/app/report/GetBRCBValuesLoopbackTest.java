package com.ysh.dlt2811bean.transport.app.report;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsGetBRCBValues;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetBRCBValues Loopback Test")
class GetBRCBValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("unknown BRCB reference returns error")
    void unknownRef() throws Exception {
        associate();

        CmsApdu response = client.getBRCBValues("C1/LLN0.Unknown");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetBRCBValues asdu = (CmsGetBRCBValues) response.getAsdu();
        assertTrue(asdu.errorBrcb.size() > 0);
        assertEquals(0, asdu.errorBrcb.get(0).getSelectedIndex());
    }

    @Test
    @DisplayName("empty reference returns error")
    void emptyRef() throws Exception {
        associate();

        CmsApdu response = client.getBRCBValues("");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetBRCBValues asdu = (CmsGetBRCBValues) response.getAsdu();
        assertTrue(asdu.errorBrcb.size() > 0);
        assertEquals(0, asdu.errorBrcb.get(0).getSelectedIndex());
    }
}
