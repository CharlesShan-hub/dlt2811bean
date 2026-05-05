package com.ysh.dlt2811bean.service.svc.rpc;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetRpcInterfaceDefinition")
class CmsGetRpcInterfaceDefinitionTest implements ServiceNameTest<CmsGetRpcInterfaceDefinition> {
    @Override public ServiceName expectedServiceName() { return ServiceName.GET_RPC_INTERFACE_DEFINITION; }
    @Override public CmsGetRpcInterfaceDefinition createAsdu() { return new CmsGetRpcInterfaceDefinition(MessageType.REQUEST); }

    @Test @DisplayName("REQUEST round-trip")
    void requestRoundTrip() throws Exception {
        CmsGetRpcInterfaceDefinition result = AsduTestUtil.roundTripViaApdu(
                new CmsGetRpcInterfaceDefinition(MessageType.REQUEST).interfaceName("IF1").reqId(1));
        assertEquals(1, result.reqId().get());
        assertEquals("IF1", result.interfaceName().get());
    }

    @Test @DisplayName("RESPONSE_POSITIVE round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetRpcInterfaceDefinition asdu = new CmsGetRpcInterfaceDefinition(MessageType.RESPONSE_POSITIVE).reqId(2);
        asdu.moreFollows().set(false);
        CmsGetRpcInterfaceDefinition result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(2, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_NEGATIVE round-trip")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetRpcInterfaceDefinition result = AsduTestUtil.roundTripViaApdu(
                new CmsGetRpcInterfaceDefinition(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).reqId(3));
        assertEquals(3, result.reqId().get());
    }
}
