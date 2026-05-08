package com.ysh.dlt2811bean.transport.app.control;

import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cancel Loopback Test")
class CancelLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("Cancel valid reference and value")
    void cancelValid() throws Exception {
        associate();

        CmsApdu response = client.cancel("E1Q1SB1/XCBR1.Pos", new CmsBoolean(true));

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("Cancel empty reference returns negative")
    void cancelEmpty() throws Exception {
        associate();

        CmsApdu response = client.cancel("", new CmsBoolean(true));

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
