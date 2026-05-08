package com.ysh.dlt2811bean.transport.app.control;

import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TimeActivatedOperate Loopback Test")
class TimeActivatedOperateLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("TimeActivatedOperate valid reference and value")
    void timeActValid() throws Exception {
        associate();

        CmsApdu response = client.timeActivatedOperate("E1Q1SB1/XCBR1.Pos", new CmsBoolean(true));

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("TimeActivatedOperate empty reference returns negative")
    void timeActEmpty() throws Exception {
        associate();

        CmsApdu response = client.timeActivatedOperate("", new CmsBoolean(true));

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
