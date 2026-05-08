package com.ysh.dlt2811bean.transport.app.goose;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.goose.CmsGetGoCBValues;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetGoCBValues Loopback Test")
class GetGoCBValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("valid GoCB reference returns GoCB data")
    void validRef() throws Exception {
        associate();

        CmsApdu response = client.getGoCBValues("C1/LLN0.ItlPositions");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetGoCBValues asdu = (CmsGetGoCBValues) response.getAsdu();
        assertTrue(asdu.errorGocb.size() > 0);
        assertEquals(1, asdu.errorGocb.get(0).getSelectedIndex());
    }

    @Test
    @DisplayName("unknown GoCB reference returns error")
    void unknownRef() throws Exception {
        associate();

        CmsApdu response = client.getGoCBValues("C1/LLN0.Unknown");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetGoCBValues asdu = (CmsGetGoCBValues) response.getAsdu();
        assertTrue(asdu.errorGocb.size() > 0);
        assertEquals(0, asdu.errorGocb.get(0).getSelectedIndex());
    }
}
