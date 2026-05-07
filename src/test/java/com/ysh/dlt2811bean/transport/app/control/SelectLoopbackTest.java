package com.ysh.dlt2811bean.transport.app.control;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Select Loopback Test")
class SelectLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("Select a valid object reference")
    void selectValid() throws Exception {
        associate();

        CmsApdu response = client.select("E1Q1SB1/XCBR1.Pos");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("Select with empty reference returns negative")
    void selectEmpty() throws Exception {
        associate();

        CmsApdu response = client.select("");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
