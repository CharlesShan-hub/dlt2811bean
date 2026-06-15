package com.ysh.dlt2811bean.transport.app.test;

import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.*;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test service (keep-alive) loopback tests.
 * Requires Associate as prerequisite.
 */
@DisplayName("Test Loopback Test")
class TestLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("Test request → echo received")
    void testPositive() throws Exception {
        associate();

        CmsApdu response = client.test();

        assertNotNull(response, "Test echo should arrive within timeout");
    }
}
