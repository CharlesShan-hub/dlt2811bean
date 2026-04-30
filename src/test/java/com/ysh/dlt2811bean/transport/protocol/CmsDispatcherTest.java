package com.ysh.dlt2811bean.transport.protocol;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;
import com.ysh.dlt2811bean.transport.io.CmsConnection;
import com.ysh.dlt2811bean.transport.protocol.handlers.AbortHandler;
import com.ysh.dlt2811bean.transport.protocol.handlers.AssociateHandler;
import com.ysh.dlt2811bean.transport.protocol.handlers.ReleaseHandler;
import com.ysh.dlt2811bean.transport.protocol.handlers.TestHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CmsDispatcher and service handlers.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CmsDispatcher")
class CmsDispatcherTest {

    @Mock
    private CmsConnection mockConnection;

    @Mock
    private Socket mockSocket;

    private CmsDispatcher dispatcher;
    private CmsServerSession session;

    @BeforeEach
    void setUp() {
        dispatcher = new CmsDispatcher();
        dispatcher.registerHandler(new AssociateHandler());
        dispatcher.registerHandler(new ReleaseHandler());
        dispatcher.registerHandler(new AbortHandler());
        dispatcher.registerHandler(new TestHandler());

        when(mockConnection.getSocket()).thenReturn(mockSocket);
        session = new CmsServerSession(mockConnection);
    }

    @Test
    @DisplayName("Associate: returns positive response with association ID")
    void associateReturnsPositiveResponse() throws Exception {
        CmsAssociate requestAsdu = new CmsAssociate(MessageType.REQUEST)
                .serverAccessPointReference("IED1", "AP1")
                .reqId(1);

        CmsApdu request = new CmsApdu(requestAsdu);
        System.out.println("Sending: " + request);

        CmsApdu response = dispatcher.dispatch(session, request);

        System.out.println("Received: " + response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsAssociate responseAsdu = (CmsAssociate) response.getAsdu();
        assertEquals(1, responseAsdu.reqId().get());
        assertNotNull(responseAsdu.associationId().get());
        assertEquals(64, responseAsdu.associationId().get().length);

        // Session should have the association ID set
        assertNotNull(session.getAssociationId());
        assertEquals(64, session.getAssociationId().length);
    }

    @Test
    @DisplayName("Test: returns positive response with empty ASDU")
    void testReturnsPositiveResponse() {
        CmsTest requestAsdu = new CmsTest();
        CmsApdu request = new CmsApdu(requestAsdu);

        CmsApdu response = dispatcher.dispatch(session, request);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("Release: returns positive response and clears association ID")
    void releaseReturnsPositiveResponseAndClearsId() {
        // First set an association ID
        byte[] testId = new byte[64];
        session.setAssociationId(testId);

        CmsRelease releaseAsdu = new CmsRelease(MessageType.REQUEST)
                .associationId(testId)
                .reqId(2);

        CmsApdu request = new CmsApdu(releaseAsdu);

        CmsApdu response = dispatcher.dispatch(session, request);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        // Association ID should be cleared
        assertNull(session.getAssociationId());
    }

    @Test
    @DisplayName("Abort: returns null (one-way service)")
    void abortReturnsNull() {
        CmsAbort abortAsdu = new CmsAbort(MessageType.REQUEST)
                .reason(0)
                .reqId(3);

        CmsApdu request = new CmsApdu(abortAsdu);

        CmsApdu response = dispatcher.dispatch(session, request);

        // Abort is one-way, no response
        assertNull(response);

        // Association ID should be cleared
        assertNull(session.getAssociationId());
    }

    @Test
    @DisplayName("Handler count is 4")
    void handlerCount() {
        assertEquals(4, dispatcher.handlerCount());
    }
}
