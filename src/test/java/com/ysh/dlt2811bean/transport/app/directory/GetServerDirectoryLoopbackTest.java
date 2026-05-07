package com.ysh.dlt2811bean.transport.app.directory;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetServerDirectory;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetServerDirectory Loopback Test")
class GetServerDirectoryLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("GetServerDirectory returns logical device list")
    void getServerDirectoryPositive() throws Exception {
        associate();

        CmsApdu response = client.getServerDirectory();
        log.info("Response: {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetServerDirectory asdu = (CmsGetServerDirectory) response.getAsdu();
        assertNotNull(asdu.reference());
        assertTrue(asdu.reference().size() > 0, "Should return at least one logical device");

        log.info("Logical devices:");
        for (int i = 0; i < asdu.reference().size(); i++) {
            log.info("  [{}] {}", i, asdu.reference().get(i).get());
        }
    }
}
