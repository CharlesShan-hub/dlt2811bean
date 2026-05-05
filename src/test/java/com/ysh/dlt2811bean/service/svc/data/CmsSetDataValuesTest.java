package com.ysh.dlt2811bean.service.svc.data;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSetDataValues")
class CmsSetDataValuesTest implements
        ServiceNameTest<CmsSetDataValues>,
        CopyTest<CmsSetDataValues> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.SET_DATA_VALUES;
    }

    @Override
    public CmsSetDataValues createAsdu() {
        return new CmsSetDataValues(MessageType.REQUEST);
    }

    @Override
    public CmsSetDataValues createCopyableAsdu() {
        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST).reqId(10);
        asdu.addData("IED1.AP1.LD1.LN1.DO1", "ST", new CmsInt32(42));
        return asdu;
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST).reqId(1);
        asdu.addData("IED1.AP1.LD1.LN1.DO1", "ST", new CmsInt32(100));
        asdu.addData("IED1.AP1.LD1.LN1.DO2", "MX", new CmsInt32(200));

        CmsSetDataValues result = AsduTestUtil.roundTripViaApdu(asdu);

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
        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST).reqId(2);
        asdu.addData("IED1.AP1.LD1.LN1.DO1", "", new CmsInt32(100));

        CmsSetDataValues result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(2, result.reqId().get());
        assertEquals(1, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertFalse(result.data().get(0).isFieldPresent("fc"));
    }

    @Test
    @DisplayName("REQUEST: empty data list")
    void requestEmptyData() throws Exception {
        CmsSetDataValues result = AsduTestUtil.roundTripViaApdu(
                new CmsSetDataValues(MessageType.REQUEST).reqId(3));

        assertEquals(3, result.reqId().get());
        assertTrue(result.data().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsSetDataValues result = AsduTestUtil.roundTripViaApdu(
                new CmsSetDataValues(MessageType.RESPONSE_POSITIVE).reqId(4));

        assertEquals(4, result.reqId().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.RESPONSE_NEGATIVE).reqId(5);
        asdu.result().add(new CmsServiceError(CmsServiceError.ACCESS_VIOLATION));
        asdu.result().add(new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE));

        CmsSetDataValues result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(5, result.reqId().get());
        assertEquals(2, result.result().size());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, result.result().get(0).get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.result().get(1).get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: empty result list")
    void negativeResponseEmpty() throws Exception {
        CmsSetDataValues result = AsduTestUtil.roundTripViaApdu(
                new CmsSetDataValues(MessageType.RESPONSE_NEGATIVE).reqId(6));

        assertEquals(6, result.reqId().get());
        assertTrue(result.result().isEmpty());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU")
    void requestRoundTripAsduOnly() throws Exception {
        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST).reqId(10);
        asdu.addData("IED1.AP1.LD1.LN1.DO1", "ST", new CmsInt32(42));

        CmsSetDataValues result = AsduTestUtil.roundTripViaAsdu(asdu);

        assertEquals(10, result.reqId().get());
        assertEquals(1, result.data().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.data().get(0).reference().get());
        assertEquals("ST", result.data().get(0).fc().get());
        assertTrue(result.data().get(0).isFieldPresent("fc"));
    }
}
