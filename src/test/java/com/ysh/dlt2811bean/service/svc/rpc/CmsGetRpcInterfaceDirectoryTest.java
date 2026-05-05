package com.ysh.dlt2811bean.service.svc.rpc;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetRpcInterfaceDirectory")
class CmsGetRpcInterfaceDirectoryTest implements ServiceNameTest<CmsGetRpcInterfaceDirectory> {
    @Override public ServiceName expectedServiceName() { return ServiceName.GET_RPC_INTERFACE_DIRECTORY; }
    @Override public CmsGetRpcInterfaceDirectory createAsdu() { return new CmsGetRpcInterfaceDirectory(MessageType.REQUEST); }

    @Test @DisplayName("REQUEST round-trip")
    void requestRoundTrip() throws Exception {
        CmsGetRpcInterfaceDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetRpcInterfaceDirectory(MessageType.REQUEST).referenceAfter("IF2").reqId(1));
        assertEquals(1, result.reqId().get());
        assertEquals("IF2", result.referenceAfter().get());
    }

    @Test @DisplayName("REQUEST without optional referenceAfter")
    void requestWithoutReferenceAfter() throws Exception {
        CmsGetRpcInterfaceDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetRpcInterfaceDirectory(MessageType.REQUEST).reqId(2));
        assertEquals(2, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_POSITIVE round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetRpcInterfaceDirectory asdu = new CmsGetRpcInterfaceDirectory(MessageType.RESPONSE_POSITIVE).reqId(3);
        asdu.addReference("IF1"); asdu.addReference("IF2");
        asdu.moreFollows().set(true);
        CmsGetRpcInterfaceDirectory result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(3, result.reqId().get());
        assertEquals(2, result.reference().size());
        assertTrue(result.moreFollows().get());
    }

    @Test @DisplayName("RESPONSE_NEGATIVE round-trip")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetRpcInterfaceDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetRpcInterfaceDirectory(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).reqId(4));
        assertEquals(4, result.reqId().get());
    }
}
