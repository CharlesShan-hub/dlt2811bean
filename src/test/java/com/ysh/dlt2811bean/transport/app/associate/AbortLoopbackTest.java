package com.ysh.dlt2811bean.transport.app.associate;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import org.junit.jupiter.api.*;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Abort service loopback tests.
 * Requires Associate as prerequisite.
 * Note: Abort is one-way, no response expected.
 */
@DisplayName("Abort Loopback Test")
class AbortLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("Abort → server closes, client sees disconnect")
    void abortNoResponse() throws Exception {
        associate();
        assertTrue(client.isConnected());

        // Abort is one-way: send() returns null without waiting
        CmsAbort request = new CmsAbort(MessageType.REQUEST).reason(0);
        CmsApdu result = client.send(request);
        assertNull(result);  // no response expected for Abort

        // Give time for server to process and close connection
        Thread.sleep(200);
        assertFalse(client.isConnected());
    }

    @Test
    @DisplayName("Re-associate after Abort → succeeds with new session")
    void reassociateAfterAbort() throws Exception {
        // First association
        associate();
        assertNotNull(client.getAssociationId());

        // Abort (one-way, no response)
        CmsAbort abort = new CmsAbort(MessageType.REQUEST).reason(0);
        assertNull(client.send(abort));
        Thread.sleep(200);
        assertFalse(client.isConnected());

        // Reconnect and re-associate
        connectClient();
        CmsApdu response = client.associate(
                new CmsAssociate(MessageType.REQUEST)
                        .serverAccessPointReference("IED1", "AP1")
                        .reqId(1));

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
    }
}
