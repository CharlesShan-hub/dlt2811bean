package com.ysh.dlt2811bean.transport.app.rpc;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcInterfaceDirectory;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetRpcInterfaceDirectory Loopback Test")
class GetRpcInterfaceDirectoryLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("Get interface directory returns all interfaces")
    void all() throws Exception {
        associate();

        CmsApdu response = client.getRpcInterfaceDirectory();

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetRpcInterfaceDirectory dir = (CmsGetRpcInterfaceDirectory) response.getAsdu();
        assertEquals(2, dir.reference.size());
        assertEquals("IF1", dir.reference.get(0).get());
        assertEquals("IF2", dir.reference.get(1).get());
    }
}
