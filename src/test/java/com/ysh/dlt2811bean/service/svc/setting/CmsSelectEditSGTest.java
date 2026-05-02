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

@DisplayName("CmsSelectEditSG")
class CmsSelectEditSGTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsSelectEditSG asdu = new CmsSelectEditSG(MessageType.REQUEST)
            .sgcbReference("IED1.AP1.LD1.LN1.SGCB1")
            .settingGroupNumber(2)
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSelectEditSG result = (CmsSelectEditSG) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.SGCB1", result.sgcbReference().get());
        assertEquals(2, result.settingGroupNumber().get());
    }

    @Test
    @DisplayName("REQUEST with default settingGroupNumber")
    void requestDefaultGroupNumber() throws Exception {
        CmsSelectEditSG asdu = new CmsSelectEditSG(MessageType.REQUEST)
            .sgcbReference("IED1.AP1.LD1.LN1.SGCB1")
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSelectEditSG result = (CmsSelectEditSG) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(1, result.settingGroupNumber().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsSelectEditSG asdu = new CmsSelectEditSG(MessageType.RESPONSE_POSITIVE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSelectEditSG result = (CmsSelectEditSG) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsSelectEditSG asdu = new CmsSelectEditSG(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(4);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsSelectEditSG result = (CmsSelectEditSG) decoded.getAsdu();
        assertEquals(4, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU static methods")
    void requestRoundTripAsduOnly() throws Exception {
        CmsSelectEditSG service = new CmsSelectEditSG(MessageType.REQUEST)
            .sgcbReference("IED1.AP1.LD1.LN1.SGCB1")
            .settingGroupNumber(3)
            .reqId(10);

        PerOutputStream pos = new PerOutputStream();
        CmsSelectEditSG.write(pos, service);

        CmsSelectEditSG result = CmsSelectEditSG.read(new PerInputStream(pos.toByteArray()), MessageType.REQUEST);

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.SGCB1", result.sgcbReference().get());
        assertEquals(3, result.settingGroupNumber().get());
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() {
        CmsSelectEditSG original = new CmsSelectEditSG(MessageType.REQUEST)
            .sgcbReference("IED1.AP1.LD1.LN1.SGCB1")
            .settingGroupNumber(4)
            .reqId(10);

        CmsSelectEditSG copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());
        assertEquals(original.sgcbReference().get(), copy.sgcbReference().get());
        assertEquals(original.settingGroupNumber().get(), copy.settingGroupNumber().get());

        copy.reqId(20);
        assertNotEquals(original.reqId(), copy.reqId());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsSelectEditSG asdu = new CmsSelectEditSG(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_POSITIVE")
    void fromFlagsPositive() {
        CmsSelectEditSG asdu = new CmsSelectEditSG(true, false);
        assertEquals(MessageType.RESPONSE_POSITIVE, asdu.messageType());
    }

    @Test
    @DisplayName("fromFlags constructor: RESPONSE_NEGATIVE")
    void fromFlagsNegative() {
        CmsSelectEditSG asdu = new CmsSelectEditSG(true, true);
        assertEquals(MessageType.RESPONSE_NEGATIVE, asdu.messageType());
    }

    @Test
    @DisplayName("getServiceCode returns SELECT_EDIT_SG")
    void serviceCode() {
        CmsSelectEditSG asdu = new CmsSelectEditSG(MessageType.REQUEST);
        assertEquals(ServiceName.SELECT_EDIT_SG, asdu.getServiceName());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsSelectEditSG asdu = new CmsSelectEditSG(MessageType.REQUEST)
            .sgcbReference("IED1.AP1.LD1.LN1.SGCB1")
            .settingGroupNumber(2)
            .reqId(1);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsSelectEditSG) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsSelectEditSG asdu = new CmsSelectEditSG(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsSelectEditSG) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}
