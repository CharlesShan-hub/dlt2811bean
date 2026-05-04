package com.ysh.dlt2811bean.service.svc.rpc;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.datatypes.CmsErrorMethodChoice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetRpcMethodDefinition")
class CmsGetRpcMethodDefinitionTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetRpcMethodDefinition asdu = new CmsGetRpcMethodDefinition(MessageType.REQUEST)
            .reqId(1);
        asdu.addReference("IF1.Method1");
        asdu.addReference("IF1.Method2");

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetRpcMethodDefinition result = (CmsGetRpcMethodDefinition) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals(2, result.reference().size());
        assertEquals("IF1.Method1", result.reference().get(0).get());
        assertEquals("IF1.Method2", result.reference().get(1).get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetRpcMethodDefinition asdu = new CmsGetRpcMethodDefinition(MessageType.RESPONSE_POSITIVE)
            .reqId(2);

        CmsErrorMethodChoice errorChoice = new CmsErrorMethodChoice()
            .selectError();
        errorChoice.error.set(CmsServiceError.ACCESS_VIOLATION);
        asdu.addErrorMethodChoice(errorChoice);

        CmsErrorMethodChoice methodChoice = new CmsErrorMethodChoice()
            .selectMethod();
        methodChoice.method.timeout.set(5000L);
        methodChoice.method.version.set(1L);
        methodChoice.method.request = ofInt32();
        methodChoice.method.response = ofBoolean();
        asdu.addErrorMethodChoice(methodChoice);
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetRpcMethodDefinition result = (CmsGetRpcMethodDefinition) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(2, result.errorMethod().size());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, result.errorMethod().get(0).error.get());
        assertEquals(5000L, result.errorMethod().get(1).method.timeout.get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetRpcMethodDefinition asdu = new CmsGetRpcMethodDefinition(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetRpcMethodDefinition result = (CmsGetRpcMethodDefinition) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("getServiceCode returns GET_RPC_METHOD_DEFINITION")
    void serviceCode() {
        CmsGetRpcMethodDefinition asdu = new CmsGetRpcMethodDefinition(MessageType.REQUEST);
        assertEquals(ServiceName.GET_RPC_METHOD_DEFINITION, asdu.getServiceName());
    }
}
