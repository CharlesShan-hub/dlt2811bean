package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSelectActiveSG")
class CmsSelectActiveSGTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsSelectActiveSG asdu = new CmsSelectActiveSG(MessageType.REQUEST)
            .sgcbReference("IED1.AP1.LD1.LN1.SGCB1")
            .settingGroupNumber(3)
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSelectActiveSG result = (CmsSelectActiveSG) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.SGCB1", result.sgcbReference().get());
        assertEquals(3, result.settingGroupNumber().get());
    }

    @Test
    @DisplayName("REQUEST with default settingGroupNumber")
    void requestDefaultGroupNumber() throws Exception {
        CmsSelectActiveSG asdu = new CmsSelectActiveSG(MessageType.REQUEST)
            .sgcbReference("IED1.AP1.LD1.LN1.SGCB1")
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSelectActiveSG result = (CmsSelectActiveSG) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(1, result.settingGroupNumber().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsSelectActiveSG asdu = new CmsSelectActiveSG(MessageType.RESPONSE_POSITIVE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSelectActiveSG result = (CmsSelectActiveSG) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsSelectActiveSG asdu = new CmsSelectActiveSG(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(4);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSelectActiveSG result = (CmsSelectActiveSG) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsSelectActiveSG service = new CmsSelectActiveSG(MessageType.REQUEST)
            .sgcbReference("IED1.AP1.LD1.LN1.SGCB1")
            .settingGroupNumber(2)
            .reqId(10);

        PerOutputStream pos = new PerOutputStream();
        CmsSelectActiveSG.write(pos, service);

        CmsSelectActiveSG result = CmsSelectActiveSG.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.SGCB1", result.sgcbReference().get());
        assertEquals(2, result.settingGroupNumber().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsSelectActiveSG original = new CmsSelectActiveSG(MessageType.REQUEST)
            .sgcbReference("IED1.AP1.LD1.LN1.SGCB1")
            .settingGroupNumber(5)
            .reqId(10);

        CmsSelectActiveSG copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(original.sgcbReference().get(), copy.sgcbReference().get());
        assertEquals(original.settingGroupNumber().get(), copy.settingGroupNumber().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsSelectActiveSG asdu = new CmsSelectActiveSG(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsSelectActiveSG asdu = new CmsSelectActiveSG(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsSelectActiveSG asdu = new CmsSelectActiveSG(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns SELECT_ACTIVE_SG")
    void serviceCode() {
        CmsSelectActiveSG asdu = new CmsSelectActiveSG(MessageType.REQUEST);
        assertEquals(ServiceName.SELECT_ACTIVE_SG, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsSelectActiveSG asdu = new CmsSelectActiveSG(MessageType.REQUEST)
            .sgcbReference("IED1.AP1.LD1.LN1.SGCB1")
            .settingGroupNumber(3)
            .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsSelectActiveSG) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsSelectActiveSG asdu = new CmsSelectActiveSG(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsSelectActiveSG) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}