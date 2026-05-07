package com.ysh.dlt2811bean.transport.app.rpc;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcMethodDefinition;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetRpcMethodDefinition Loopback Test")
class GetRpcMethodDefinitionLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("Get method definition for known methods")
    void knownMethods() throws Exception {
        associate();

        CmsApdu response = client.getRpcMethodDefinition("IF1.Method1", "IF2.status");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetRpcMethodDefinition rpc = (CmsGetRpcMethodDefinition) response.getAsdu();
        assertEquals(2, rpc.errorMethod.size());
        assertEquals(1, rpc.errorMethod.get(0).getSelectedIndex());
        assertEquals(1, rpc.errorMethod.get(1).getSelectedIndex());
    }

    @Test
    @DisplayName("Get method definition — unknown method returns error")
    void unknownMethod() throws Exception {
        associate();

        CmsApdu response = client.getRpcMethodDefinition("IF1.Nonexistent");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetRpcMethodDefinition rpc = (CmsGetRpcMethodDefinition) response.getAsdu();
        assertEquals(1, rpc.errorMethod.size());
        assertEquals(0, rpc.errorMethod.get(0).getSelectedIndex());
    }

    @Test
    @DisplayName("Get method definition — mixed known and unknown")
    void mixedMethods() throws Exception {
        associate();

        CmsApdu response = client.getRpcMethodDefinition("IF1.Method1", "bad.Ref", "IF2.reset");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetRpcMethodDefinition rpc = (CmsGetRpcMethodDefinition) response.getAsdu();
        assertEquals(3, rpc.errorMethod.size());
        assertEquals(1, rpc.errorMethod.get(0).getSelectedIndex());
        assertEquals(0, rpc.errorMethod.get(1).getSelectedIndex());
        assertEquals(1, rpc.errorMethod.get(2).getSelectedIndex());
    }

    @Test
    @DisplayName("Get method definition — no references returns negative")
    void noReferences() throws Exception {
        associate();

        CmsApdu response = client.getRpcMethodDefinition();

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
