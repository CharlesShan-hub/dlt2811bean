package com.ysh.dlt2811bean.transport.app.control;

import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SelectWithValue Loopback Test")
class SelectWithValueLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("SelectWithValue valid reference and value")
    void selectValid() throws Exception {
        associate();

        CmsApdu response = client.selectWithValue("E1Q1SB1/XCBR1.Pos", new CmsBoolean(true));

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("SelectWithValue empty reference returns negative")
    void selectEmpty() throws Exception {
        associate();

        CmsApdu response = client.selectWithValue("", new CmsBoolean(true));

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
