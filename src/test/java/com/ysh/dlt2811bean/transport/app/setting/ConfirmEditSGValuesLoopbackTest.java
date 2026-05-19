package com.ysh.dlt2811bean.transport.app.setting;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsConfirmEditSGValues;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConfirmEditSGValues Loopback Test")
class ConfirmEditSGValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("confirm edit SG values returns Response+")
    void validRef() throws Exception {
        associate();

        CmsApdu response = client.send(new CmsConfirmEditSGValues(MessageType.REQUEST).sgcbReference("C1/LLN0.SGCB"));

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("empty reference returns Response-")
    void emptyRef() throws Exception {
        associate();

        CmsApdu response = client.send(new CmsConfirmEditSGValues(MessageType.REQUEST).sgcbReference(""));

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
