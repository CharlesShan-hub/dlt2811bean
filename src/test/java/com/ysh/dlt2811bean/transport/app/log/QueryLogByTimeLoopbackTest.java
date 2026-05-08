package com.ysh.dlt2811bean.transport.app.log;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsQueryLogByTime;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("QueryLogByTime Loopback Test")
class QueryLogByTimeLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("valid log reference returns Response+ with empty entries")
    void validRef() throws Exception {
        associate();

        CmsApdu response = client.queryLogByTime("C1/LLN0.Log");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsQueryLogByTime asdu = (CmsQueryLogByTime) response.getAsdu();
        assertNotNull(asdu.logEntry());
    }

    @Test
    @DisplayName("invalid log reference returns Response-")
    void invalidRef() throws Exception {
        associate();

        CmsApdu response = client.queryLogByTime("C1/LLN0.Unknown");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
