package com.ysh.dlt2811bean.service.svc.rpc;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetRpcMethodDirectory")
class CmsGetRpcMethodDirectoryTest implements ServiceNameTest<CmsGetRpcMethodDirectory> {
    @Override public ServiceName expectedServiceName() { return ServiceName.GET_RPC_METHOD_DIRECTORY; }
    @Override public CmsGetRpcMethodDirectory createAsdu() { return new CmsGetRpcMethodDirectory(MessageType.REQUEST); }

    @Test @DisplayName("REQUEST round-trip")
    void requestRoundTrip() throws Exception {
        CmsGetRpcMethodDirectory asdu = new CmsGetRpcMethodDirectory(MessageType.REQUEST)
                .interfaceName("IF1").reqId(1);
        CmsGetRpcMethodDirectory result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(1, result.reqId().get());
        assertNotNull(result.interfaceName());
    }

    @Test @DisplayName("RESPONSE_POSITIVE round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetRpcMethodDirectory asdu = new CmsGetRpcMethodDirectory(MessageType.RESPONSE_POSITIVE).reqId(2);
        asdu.addReference("Method1");
        asdu.addReference("Method2");
        asdu.moreFollows().set(true);
        CmsGetRpcMethodDirectory result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(2, result.reqId().get());
        assertEquals(2, result.reference().size());
    }

    @Test @DisplayName("RESPONSE_NEGATIVE round-trip")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetRpcMethodDirectory result = AsduTestUtil.roundTripViaApdu(
                new CmsGetRpcMethodDirectory(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).reqId(3));
        assertEquals(3, result.reqId().get());
    }
}
