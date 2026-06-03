package com.ysh.dlt2811bean.service.svc.data;

import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataDefinitionEntry;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetDataDefinition")
class CmsGetDataDefinitionTest implements
        ServiceNameTest<CmsGetDataDefinition>,
        CopyTest<CmsGetDataDefinition> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.GET_DATA_DEFINITION;
    }

    @Override
    public CmsGetDataDefinition createAsdu() {
        return new CmsGetDataDefinition(MessageType.REQUEST);
    }

    @Override
    public CmsGetDataDefinition createCopyableAsdu() {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.REQUEST).reqId(10);
        asdu.addData("IED1.AP1.LD1.LN1.DO1", "ST");
        return asdu;
    }

    // ==================== REQUEST tests ====================

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.REQUEST).reqId(1);
        asdu.addData("IED1.AP1.LD1.LN1.DO1", "ST");
        asdu.addData("IED1.AP1.LD1.LN1.DO2", "MX");

        CmsGetDataDefinition result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(1, result.reqId().get());
        assertEquals(2, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertEquals("ST", result.data().get(0).fc().get());
        assertTrue(result.data().get(0).isFieldPresent("fc"));
        assertEquals("IED1.AP1.LD1.LN1.DO2", result.data().get(1).reference().get());
        assertEquals("MX", result.data().get(1).fc().get());
    }

    @Test
    @DisplayName("REQUEST without optional fc")
    void requestWithoutFc() throws Exception {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.REQUEST).reqId(2);
        asdu.addData("IED1.AP1.LD1.LN1.DO1");

        CmsGetDataDefinition result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(2, result.reqId().get());
        assertEquals(1, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertFalse(result.data().get(0).isFieldPresent("fc"));
    }

    @Test
    @DisplayName("REQUEST: empty data list")
    void requestEmptyData() throws Exception {
        CmsGetDataDefinition result = AsduTestUtil.roundTripViaApdu(
                new CmsGetDataDefinition(MessageType.REQUEST).reqId(3));

        assertEquals(3, result.reqId().get());
        assertTrue(result.data().isEmpty());
    }

    // ==================== RESPONSE_POSITIVE tests ====================

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.RESPONSE_POSITIVE).reqId(4);
        asdu.definition().add(new CmsGetDataDefinitionEntry()
                .cdcType("INC")
                .definition(ofInt32()));
        asdu.definition().add(new CmsGetDataDefinitionEntry()
                .definition(ofBoolean()));
        asdu.moreFollows().set(true);

        CmsGetDataDefinition result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(4, result.reqId().get());
        assertEquals(2, result.definition().size());
        assertTrue(result.definition().get(0).isFieldPresent("cdcType"));
        assertEquals("INC", result.definition().get(0).cdcType().get());
        assertEquals(CmsDataDefinition.INT32, result.definition().get(0).definition().getChoiceIndex());
        assertFalse(result.definition().get(1).isFieldPresent("cdcType"));
        assertEquals(CmsDataDefinition.BOOLEAN, result.definition().get(1).definition().getChoiceIndex());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty definition list")
    void positiveResponseEmpty() throws Exception {
        CmsGetDataDefinition asdu2 = new CmsGetDataDefinition(MessageType.RESPONSE_POSITIVE).reqId(5);
        asdu2.moreFollows().set(false);
        CmsGetDataDefinition result = AsduTestUtil.roundTripViaApdu(asdu2);

        assertEquals(5, result.reqId().get());
        assertTrue(result.definition().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    // ==================== RESPONSE_NEGATIVE tests ====================

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetDataDefinition result = AsduTestUtil.roundTripViaApdu(
                new CmsGetDataDefinition(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                        .reqId(6));

        assertEquals(6, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    // ==================== ASDU-only round-trip ====================

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.REQUEST).reqId(10);
        asdu.addData("IED1.AP1.LD1.LN1.DO1", "ST");

        CmsGetDataDefinition result = AsduTestUtil.roundTripViaAsdu(asdu);

        assertEquals(10, result.reqId().get());
        assertEquals(1, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertEquals("ST", result.data().get(0).fc().get());
    }
}
