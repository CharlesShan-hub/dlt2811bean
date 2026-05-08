package com.ysh.dlt2811bean.transport.app.control;

import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TimeActivatedOperateTermination Loopback Test")
class TimeActivatedOperateTerminationLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("TimeActivatedOperateTermination valid reference")
    void timeActTermValid() throws Exception {
        associate();

        CmsApdu response = client.timeActivatedOperateTermination("E1Q1SB1/XCBR1.Pos", new CmsBoolean(true));

        assertEquals(MessageType.REQUEST_POSITIVE, response.getMessageType());
    }
}
