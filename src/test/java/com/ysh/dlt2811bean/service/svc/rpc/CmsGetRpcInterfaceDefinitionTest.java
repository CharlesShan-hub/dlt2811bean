package com.ysh.dlt2811bean.service.svc.rpc;

import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.datatypes.CmsRpcMethodDefEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetRpcInterfaceDefinition")
class CmsGetRpcInterfaceDefinitionTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetRpcInterfaceDefinition asdu = new CmsGetRpcInterfaceDefinition(MessageType.REQUEST)
            .interfaceName("IF1")
            .referenceAfter("M2")
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetRpcInterfaceDefinition result = (CmsGetRpcInterfaceDefinition) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IF1", result.interfaceName().get());
        assertTrue(result.isFieldPresent("referenceAfter"));
    }

    @Test
    @DisplayName("REQUEST without optional referenceAfter")
    void requestWithoutReferenceAfter() throws Exception {
        CmsGetRpcInterfaceDefinition asdu = new CmsGetRpcInterfaceDefinition(MessageType.REQUEST)
            .interfaceName("IF1")
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetRpcInterfaceDefinition result = (CmsGetRpcInterfaceDefinition) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals("IF1", result.interfaceName().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetRpcInterfaceDefinition asdu = new CmsGetRpcInterfaceDefinition(MessageType.RESPONSE_POSITIVE)
            .reqId(3);

        CmsRpcMethodDefEntry entry = new CmsRpcMethodDefEntry();
        entry.name.set("TestMethod");
        entry.version.set(1L);
        entry.timeout.set(5000L);
        entry.request = ofInt32();
        entry.response = ofBoolean();
        asdu.method().add(entry);
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetRpcInterfaceDefinition result = (CmsGetRpcInterfaceDefinition) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(1, result.method().size());
        assertEquals("TestMethod", result.method().get(0).name().get());
        assertEquals(1L, result.method().get(0).version().get());
        assertEquals(5000L, result.method().get(0).timeout().get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetRpcInterfaceDefinition asdu = new CmsGetRpcInterfaceDefinition(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(4);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetRpcInterfaceDefinition result = (CmsGetRpcInterfaceDefinition) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("getServiceCode returns GET_RPC_INTERFACE_DEFINITION")
    void serviceCode() {
        CmsGetRpcInterfaceDefinition asdu = new CmsGetRpcInterfaceDefinition(MessageType.REQUEST);
        assertEquals(ServiceName.GET_RPC_INTERFACE_DEFINITION, asdu.getServiceName());
    }
}
