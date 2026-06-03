package com.ysh.dlt2811bean.service.svc.data;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.FromFlagsTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetDataValues")
class CmsGetDataValuesTest implements
        ServiceNameTest<CmsGetDataValues>,
        CopyTest<CmsGetDataValues>,
        FromFlagsTest<CmsGetDataValues> {

    // ==================== Mixin factory methods ====================

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.GET_DATA_VALUES;
    }

    @Override
    public CmsGetDataValues createAsdu() {
        return new CmsGetDataValues(MessageType.REQUEST);
    }

    @Override
    public CmsGetDataValues createCopyableAsdu() {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.REQUEST).reqId(10);
        asdu.data().add(new CmsGetDataValuesEntry()
                .reference("IED1.AP1.LD1.LN1.DO1")
                .fc("ST"));
        return asdu;
    }

    @Override
    public CmsGetDataValues createFromFlags(boolean resp, boolean err) {
        return new CmsGetDataValues(resp, err);
    }

    // ==================== REQUEST tests ====================

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.REQUEST).reqId(1);
        asdu.data().add(new CmsGetDataValuesEntry()
                .reference("IED1.AP1.LD1.LN1.DO1").fc("ST"));
        asdu.data().add(new CmsGetDataValuesEntry()
                .reference("IED1.AP1.LD1.LN1.DO2").fc("MX"));

        CmsGetDataValues result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(1, result.reqId().get());
        assertEquals(2, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertEquals("ST", result.data().get(0).fc().get());
        assertEquals("IED1.AP1.LD1.LN1.DO2", result.data().get(1).reference().get());
        assertEquals("MX", result.data().get(1).fc().get());
        assertTrue(result.data().get(0).isFieldPresent("fc"));
        assertTrue(result.data().get(1).isFieldPresent("fc"));
    }

    @Test
    @DisplayName("REQUEST without optional fc")
    void requestWithoutFc() throws Exception {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.REQUEST).reqId(2);
        asdu.data().add(new CmsGetDataValuesEntry()
                .reference("IED1.AP1.LD1.LN1.DO1"));

        CmsGetDataValues result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(2, result.reqId().get());
        assertEquals(1, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertFalse(result.data().get(0).isFieldPresent("fc"));
        assertTrue(result.data().get(0).fc().get().isEmpty());
    }

    @Test
    @DisplayName("REQUEST: empty data list")
    void requestEmptyData() throws Exception {
        CmsGetDataValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetDataValues(MessageType.REQUEST).reqId(3));

        assertEquals(3, result.reqId().get());
        assertTrue(result.data().isEmpty());
    }

    // ==================== RESPONSE_POSITIVE tests ====================

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.RESPONSE_POSITIVE).reqId(4);
        asdu.value().add(new CmsInt32(100));
        asdu.value().add(new CmsInt32(200));
        asdu.moreFollows().set(true);

        CmsGetDataValues result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(4, result.reqId().get());
        assertEquals(2, result.value().size());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty value list")
    void positiveResponseEmpty() throws Exception {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.RESPONSE_POSITIVE).reqId(5);
        asdu.moreFollows().set(false);
        CmsGetDataValues result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(5, result.reqId().get());
        assertTrue(result.value().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    // ==================== RESPONSE_NEGATIVE tests ====================

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetDataValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetDataValues(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                        .reqId(6));

        assertEquals(6, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    // ==================== ASDU-only round-trip ====================

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.REQUEST).reqId(10);
        asdu.data().add(new CmsGetDataValuesEntry()
                .reference("IED1.AP1.LD1.LN1.DO1").fc("ST"));

        CmsGetDataValues result = AsduTestUtil.roundTripViaAsdu(asdu);

        assertEquals(10, result.reqId().get());
        assertEquals(1, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertEquals("ST", result.data().get(0).fc().get());
        assertTrue(result.data().get(0).isFieldPresent("fc"));
    }

    // ==================== toString tests ====================

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.REQUEST).reqId(1);
        asdu.data().add(new CmsGetDataValuesEntry()
                .reference("IED1.AP1.LD1.LN1.DO1").fc("ST"));

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetDataValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.RESPONSE_NEGATIVE)
                .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                .reqId(2);

        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsGetDataValues) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}
