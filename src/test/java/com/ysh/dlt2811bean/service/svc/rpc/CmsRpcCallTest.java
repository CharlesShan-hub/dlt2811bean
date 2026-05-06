package com.ysh.dlt2811bean.service.svc.rpc;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsRpcCall")
class CmsRpcCallTest {

    @Test
    @DisplayName("REQUEST with reqData: encode and decode round-trip via APDU")
    void requestWithReqData() throws Exception {
        CmsRpcCall asdu = new CmsRpcCall(MessageType.REQUEST)
            .method("IF1.Method1")
            .reqData(new CmsInt32(42))
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsRpcCall result = (CmsRpcCall) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IF1.Method1", result.method().get());
        assertEquals(42, ((CmsInt32) ((CmsData<?>) result.reqDataCallID().reqData).get()).get());
    }

    @Test
    @DisplayName("REQUEST with callID: encode and decode round-trip via APDU")
    void requestWithCallID() throws Exception {
        CmsRpcCall asdu = new CmsRpcCall(MessageType.REQUEST)
            .method("IF1.Method1")
            .callID(new byte[]{0x01, 0x02, 0x03})
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsRpcCall result = (CmsRpcCall) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals("IF1.Method1", result.method().get());
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, result.reqDataCallID().callID.get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsRpcCall asdu = new CmsRpcCall(MessageType.RESPONSE_POSITIVE)
            .rspData(new CmsInt32(100))
            .nextCallID(new byte[]{0x0A, 0x0B})
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsRpcCall result = (CmsRpcCall) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertTrue(result.isFieldPresent("nextCallID"));
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE without optional nextCallID")
    void positiveResponseWithoutNextCallID() throws Exception {
        CmsRpcCall asdu = new CmsRpcCall(MessageType.RESPONSE_POSITIVE)
            .rspData(new CmsInt32(100))
            .reqId(4);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsRpcCall result = (CmsRpcCall) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertFalse(result.isFieldPresent("nextCallID"));
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsRpcCall asdu = new CmsRpcCall(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(5);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsRpcCall result = (CmsRpcCall) decoded.getAsdu();
        assertEquals(5, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("getServiceCode returns RPC_CALL")
    void serviceCode() {
        CmsRpcCall asdu = new CmsRpcCall(MessageType.REQUEST);
        assertEquals(ServiceName.RPC_CALL, asdu.getServiceName());
    }
}
