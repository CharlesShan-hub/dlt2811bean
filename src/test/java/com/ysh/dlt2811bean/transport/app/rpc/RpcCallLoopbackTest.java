package com.ysh.dlt2811bean.transport.app.rpc;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsRpcCall;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RpcCall Loopback Test")
class RpcCallLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("RpcCall ping → pong")
    void ping() throws Exception {
        associate();

        CmsApdu response = client.rpcCall("ping", new CmsInt32U(0));

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsRpcCall rpc = (CmsRpcCall) response.getAsdu();
        assertNotNull(rpc.rspData);
    }

    @Test
    @DisplayName("RpcCall echo → response matches request")
    void echo() throws Exception {
        associate();

        CmsApdu response = client.rpcCall("echo", new CmsVisibleString("hello").max(255));

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsRpcCall rpc = (CmsRpcCall) response.getAsdu();
        assertNotNull(rpc.rspData);
    }

    @Test
    @DisplayName("RpcCall iterate → pagination with continuation")
    void iterate() throws Exception {
        associate();

        CmsApdu response = client.rpcCall("iterate", new CmsInt32U(0));
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsRpcCall rpc = (CmsRpcCall) response.getAsdu();
        assertNotNull(rpc.rspData);

        if (rpc.nextCallID != null && rpc.nextCallID.get() != null && rpc.nextCallID.get().length > 0) {
            byte[] nextId = rpc.nextCallID.get();
            CmsApdu nextResponse = client.rpcCall("iterate", nextId);
            assertEquals(MessageType.RESPONSE_POSITIVE, nextResponse.getMessageType());
        }
    }

    @Test
    @DisplayName("RpcCall unknown method → negative response")
    void unknownMethod() throws Exception {
        associate();

        CmsApdu response = client.rpcCall("nonexistent", new CmsInt32U(0));

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
