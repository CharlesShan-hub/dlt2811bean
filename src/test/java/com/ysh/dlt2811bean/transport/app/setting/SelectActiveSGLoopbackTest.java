package com.ysh.dlt2811bean.transport.app.setting;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsSelectActiveSG;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SelectActiveSG Loopback Test")
class SelectActiveSGLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("select active SG returns Response+")
    void validRequest() throws Exception {
        associate();

        CmsSelectActiveSG asduReq = new CmsSelectActiveSG(MessageType.REQUEST)
                .sgcbReference("C1/LLN0.SGCB")
                .settingGroupNumber(1);
        CmsApdu response = client.send(asduReq);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("empty reference returns Response-")
    void emptyRef() throws Exception {
        associate();

        CmsSelectActiveSG asduReq = new CmsSelectActiveSG(MessageType.REQUEST)
                .sgcbReference("")
                .settingGroupNumber(1);
        CmsApdu response = client.send(asduReq);

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
