package com.ysh.dlt2811bean.transport.app.associate;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Associate service loopback tests.
 * No prerequisite - tests connection establishment from scratch.
 */
@DisplayName("Associate Loopback Test")
class AssociateLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("Associate → positive response + 64-byte associationId")
    void associatePositive() throws Exception {
        CmsApdu response = client.associate();
        //log.info(response.toString());

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
        assertEquals(64, client.getAssociationId().length);
    }

    @Test
    @DisplayName("associate(ap, ep) → positive response with specified access point")
    void associateWithAccessPoint() throws Exception {
        CmsApdu response = client.associate("IED1", "AP1");
        
        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
    }
}
