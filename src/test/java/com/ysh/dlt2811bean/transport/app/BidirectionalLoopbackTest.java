package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.junit.jupiter.api.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bidirectional Communication Loopback Test")
class BidirectionalLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("Server push CommandTermination → Client auto-responds")
    void serverPushCommandTermination() throws Exception {
        associate();

        // Get the server session
        Collection<CmsServerSession> sessions = server.getSessions();
        assertEquals(1, sessions.size());
        CmsServerSession serverSession = sessions.iterator().next();

        // Server pushes CommandTermination
        server.pushCommandTermination(serverSession, "E1Q1SB1/XCBR1.Pos", new CmsBoolean(true));

        // Give the client a moment to process and respond
        Thread.sleep(100);

        verifyNoErrors();
    }

    @Test
    @DisplayName("Server push TimeActivatedOperateTermination → Client auto-responds")
    void serverPushTimeActTerm() throws Exception {
        associate();

        Collection<CmsServerSession> sessions = server.getSessions();
        assertEquals(1, sessions.size());
        CmsServerSession serverSession = sessions.iterator().next();

        server.pushTimeActivatedOperateTermination(serverSession, "E1Q1SB1/XCBR1.Pos", new CmsBoolean(true));

        Thread.sleep(100);

        verifyNoErrors();
    }

    @Test
    @DisplayName("Normal request still works alongside push")
    void normalRequestAfterPush() throws Exception {
        associate();

        // First do a normal request
        CmsApdu dirResponse = client.getServerDirectory();
        assertEquals(MessageType.RESPONSE_POSITIVE, dirResponse.getMessageType());

        // Then server pushes
        Collection<CmsServerSession> sessions = server.getSessions();
        CmsServerSession serverSession = sessions.iterator().next();
        server.pushCommandTermination(serverSession, "E1Q1SB1/XCBR1.Pos", new CmsBoolean(true));

        Thread.sleep(100);

        // Normal request still works
        CmsApdu valResponse = client.getServerDirectory();
        assertEquals(MessageType.RESPONSE_POSITIVE, valResponse.getMessageType());

        verifyNoErrors();
    }

    private void verifyNoErrors() {
        // Session should still be associated after push
        assertTrue(client.isConnected(), "Client should still be connected after push");
        assertNotNull(client.getAssociationId(), "Client should still be associated after push");
    }
}