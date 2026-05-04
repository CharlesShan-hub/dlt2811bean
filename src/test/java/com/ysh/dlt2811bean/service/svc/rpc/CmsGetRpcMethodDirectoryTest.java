package com.ysh.dlt2811bean.service.svc.rpc;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetRpcMethodDirectory")
class CmsGetRpcMethodDirectoryTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetRpcMethodDirectory asdu = new CmsGetRpcMethodDirectory(MessageType.REQUEST)
            .interfaceName("IF1")
            .referenceAfter("Method2")
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetRpcMethodDirectory result = (CmsGetRpcMethodDirectory) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IF1", result.interfaceName().get());
        assertEquals("Method2", result.referenceAfter().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetRpcMethodDirectory asdu = new CmsGetRpcMethodDirectory(MessageType.RESPONSE_POSITIVE)
            .reqId(2);
        asdu.addReference("Method1");
        asdu.addReference("Method2");
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetRpcMethodDirectory result = (CmsGetRpcMethodDirectory) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(2, result.reference().size());
        assertEquals("Method1", result.reference().get(0).get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetRpcMethodDirectory asdu = new CmsGetRpcMethodDirectory(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetRpcMethodDirectory result = (CmsGetRpcMethodDirectory) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("getServiceCode returns GET_RPC_METHOD_DIRECTORY")
    void serviceCode() {
        CmsGetRpcMethodDirectory asdu = new CmsGetRpcMethodDirectory(MessageType.REQUEST);
        assertEquals(ServiceName.GET_RPC_METHOD_DIRECTORY, asdu.getServiceName());
    }
}
