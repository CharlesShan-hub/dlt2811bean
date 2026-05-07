package com.ysh.dlt2811bean.transport.app.rpc;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcInterfaceDefinition;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetRpcInterfaceDefinition Loopback Test")
class GetRpcInterfaceDefinitionLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("Get interface definition for IF1")
    void if1() throws Exception {
        associate();

        CmsApdu response = client.getRpcInterfaceDefinition("IF1");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetRpcInterfaceDefinition def = (CmsGetRpcInterfaceDefinition) response.getAsdu();
        assertEquals(2, def.method.size());
        assertEquals("IF1.Method1", def.method.get(0).name.get());
        assertEquals("IF1.Method2", def.method.get(1).name.get());
    }

    @Test
    @DisplayName("Get interface definition for IF2")
    void if2() throws Exception {
        associate();

        CmsApdu response = client.getRpcInterfaceDefinition("IF2");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetRpcInterfaceDefinition def = (CmsGetRpcInterfaceDefinition) response.getAsdu();
        assertEquals(2, def.method.size());
        assertEquals("IF2.status", def.method.get(0).name.get());
        assertEquals("IF2.reset", def.method.get(1).name.get());
    }

    @Test
    @DisplayName("Get interface definition — unknown interface returns negative")
    void unknownInterface() throws Exception {
        associate();

        CmsApdu response = client.getRpcInterfaceDefinition("IF99");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
