package com.ysh.dlt2811bean.transport.app.rpc;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcMethodDirectory;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetRpcMethodDirectory Loopback Test")
class GetRpcMethodDirectoryLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("Get method directory for IF1")
    void if1() throws Exception {
        associate();

        CmsApdu response = client.getRpcMethodDirectory("IF1");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetRpcMethodDirectory dir = (CmsGetRpcMethodDirectory) response.getAsdu();
        assertEquals(2, dir.reference.size());
        assertEquals("IF1.Method1", dir.reference.get(0).get());
        assertEquals("IF1.Method2", dir.reference.get(1).get());
    }

    @Test
    @DisplayName("Get method directory for IF2")
    void if2() throws Exception {
        associate();

        CmsApdu response = client.getRpcMethodDirectory("IF2");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetRpcMethodDirectory dir = (CmsGetRpcMethodDirectory) response.getAsdu();
        assertEquals(2, dir.reference.size());
        assertEquals("IF2.status", dir.reference.get(0).get());
        assertEquals("IF2.reset", dir.reference.get(1).get());
    }

    @Test
    @DisplayName("Get method directory for all interfaces")
    void all() throws Exception {
        associate();

        CmsApdu response = client.getRpcMethodDirectory();

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetRpcMethodDirectory dir = (CmsGetRpcMethodDirectory) response.getAsdu();
        assertEquals(4, dir.reference.size());
    }

    @Test
    @DisplayName("Get method directory — unknown interface returns negative")
    void unknownInterface() throws Exception {
        associate();

        CmsApdu response = client.getRpcMethodDirectory("IF99");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
