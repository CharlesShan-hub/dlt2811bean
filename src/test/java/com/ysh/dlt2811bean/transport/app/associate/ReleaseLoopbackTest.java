package com.ysh.dlt2811bean.transport.app.associate;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Release service loopback tests.
 * Requires Associate as prerequisite.
 */
@DisplayName("Release Loopback Test")
class ReleaseLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("Associate → Release → positive response, associationId cleared")
    void releasePositive() throws Exception {
        startServer(false);
        startClient(false);

        associate();

        CmsApdu response = client.release();
        log.info(response.toString());

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNull(client.getAssociationId());

        closeClient();
        closeServer();
    }
}
