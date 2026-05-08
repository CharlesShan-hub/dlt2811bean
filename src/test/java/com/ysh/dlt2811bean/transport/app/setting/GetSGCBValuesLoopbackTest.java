package com.ysh.dlt2811bean.transport.app.setting;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetSGCBValues;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetSGCBValues Loopback Test")
class GetSGCBValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("get SGCB values returns SGCB data")
    void validRef() throws Exception {
        associate();

        CmsApdu response = client.getSGCBValues("C1/LLN0.SGCB");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetSGCBValues asdu = (CmsGetSGCBValues) response.getAsdu();
        assertTrue(asdu.errorSgcb.size() > 0);
        assertEquals(1, asdu.errorSgcb.get(0).getSelectedIndex());
    }

    @Test
    @DisplayName("empty reference returns error")
    void emptyRef() throws Exception {
        associate();

        CmsApdu response = client.getSGCBValues("");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetSGCBValues asdu = (CmsGetSGCBValues) response.getAsdu();
        assertEquals(0, asdu.errorSgcb.get(0).getSelectedIndex());
    }
}
