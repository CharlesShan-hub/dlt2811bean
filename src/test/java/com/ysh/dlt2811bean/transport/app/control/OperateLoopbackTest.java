package com.ysh.dlt2811bean.transport.app.control;

import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Operate Loopback Test")
class OperateLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("Operate valid reference and value")
    void operateValid() throws Exception {
        associate();

        CmsApdu response = client.operate("E1Q1SB1/XCBR1.Pos", new CmsBoolean(true));

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("Operate empty reference returns negative")
    void operateEmpty() throws Exception {
        associate();

        CmsApdu response = client.operate("", new CmsBoolean(true));

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
