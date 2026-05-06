package com.ysh.dlt2811bean.transport.app.associate;

import org.junit.jupiter.api.*;

import com.ysh.dlt2811bean.service.svc.association.datatypes.AbortReason;
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

        client.abort(); // default AbortReason.OTHER (0)

        assertFalse(client.isConnected());
    }

    @Test
    @DisplayName("Abort → server closes, client sees disconnect")
    void abortWithReason() throws Exception {
        associate();

        client.abort(AbortReason.INVALID_REQ_ID);

        assertFalse(client.isConnected());
    }
}
