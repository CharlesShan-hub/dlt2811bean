package com.ysh.dlt2811bean.transport.app.sv;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.sv.CmsGetMSVCBValues;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetMSVCBValues Loopback Test")
class GetMSVCBValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("GetMSVCBValues with valid reference returns MSVCB data")
    void validRef() throws Exception {
        associate();

        CmsApdu response = client.getMSVCBValues("C1/LLN0.Volt");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetMSVCBValues asdu = (CmsGetMSVCBValues) response.getAsdu();
        assertTrue(asdu.errorMsvcb.size() > 0);
        assertEquals(1, asdu.errorMsvcb.get(0).getSelectedIndex()); // msvcb selected
    }

    @Test
    @DisplayName("GetMSVCBValues with unknown reference returns error")
    void unknownRef() throws Exception {
        associate();

        CmsApdu response = client.getMSVCBValues("C1/LLN0.Unknown");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetMSVCBValues asdu = (CmsGetMSVCBValues) response.getAsdu();
        assertEquals(0, asdu.errorMsvcb.get(0).getSelectedIndex()); // error selected
    }
}
