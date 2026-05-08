package com.ysh.dlt2811bean.transport.app.data;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDirectory;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetDataDirectory Loopback Test")
class GetDataDirectoryLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("valid data reference returns directory with entries (§8.4.3.2)")
    void validRef() throws Exception {
        associate();

        CmsApdu response = client.getDataDirectory("C1/LPHD1.Proxy");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetDataDirectory asdu = (CmsGetDataDirectory) response.getAsdu();
        assertTrue(asdu.dataAttribute.size() > 0);
    }

    @Test
    @DisplayName("unknown data reference returns Response-")
    void unknownRef() throws Exception {
        associate();

        CmsApdu response = client.getDataDirectory("C1/LPHD1.Unknown");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }

    @Test
    @DisplayName("empty reference returns Response-")
    void emptyRef() throws Exception {
        associate();

        CmsApdu response = client.getDataDirectory("");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
