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

@DisplayName("CmsGetRpcInterfaceDirectory")
class CmsGetRpcInterfaceDirectoryTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetRpcInterfaceDirectory asdu = new CmsGetRpcInterfaceDirectory(MessageType.REQUEST)
            .referenceAfter("IF2")
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetRpcInterfaceDirectory result = (CmsGetRpcInterfaceDirectory) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IF2", result.referenceAfter().get());
    }

    @Test
    @DisplayName("REQUEST without optional referenceAfter")
    void requestWithoutReferenceAfter() throws Exception {
        CmsGetRpcInterfaceDirectory asdu = new CmsGetRpcInterfaceDirectory(MessageType.REQUEST)
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetRpcInterfaceDirectory result = (CmsGetRpcInterfaceDirectory) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetRpcInterfaceDirectory asdu = new CmsGetRpcInterfaceDirectory(MessageType.RESPONSE_POSITIVE)
            .reqId(3);
        asdu.addReference("IF1");
        asdu.addReference("IF2");
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetRpcInterfaceDirectory result = (CmsGetRpcInterfaceDirectory) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(2, result.reference().size());
        assertEquals("IF1", result.reference().get(0).get());
        assertEquals("IF2", result.reference().get(1).get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetRpcInterfaceDirectory asdu = new CmsGetRpcInterfaceDirectory(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(4);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetRpcInterfaceDirectory result = (CmsGetRpcInterfaceDirectory) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("getServiceCode returns GET_RPC_INTERFACE_DIRECTORY")
    void serviceCode() {
        CmsGetRpcInterfaceDirectory asdu = new CmsGetRpcInterfaceDirectory(MessageType.REQUEST);
        assertEquals(ServiceName.GET_RPC_INTERFACE_DIRECTORY, asdu.getServiceName());
    }
}
