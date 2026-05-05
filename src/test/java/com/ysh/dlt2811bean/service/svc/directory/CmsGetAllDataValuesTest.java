package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataEntry;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.FromFlagsTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetAllDataValues")
class CmsGetAllDataValuesTest implements
        ServiceNameTest<CmsGetAllDataValues>,
        CopyTest<CmsGetAllDataValues>,
        FromFlagsTest<CmsGetAllDataValues> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.GET_ALL_DATA_VALUES;
    }

    @Override
    public CmsGetAllDataValues createAsdu() {
        return new CmsGetAllDataValues(MessageType.REQUEST);
    }

    @Override
    public CmsGetAllDataValues createCopyableAsdu() {
        return new CmsGetAllDataValues(MessageType.REQUEST)
                .ldName("IED1.AP1.LD1")
                .fc("ST")
                .referenceAfter("IED1.AP1.LD1.LN1.DO1")
                .reqId(10);
    }

    @Override
    public CmsGetAllDataValues createFromFlags(boolean resp, boolean err) {
        return new CmsGetAllDataValues(resp, err);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetAllDataValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetAllDataValues(MessageType.REQUEST)
                        .ldName("IED1.AP1.LD1")
                        .fc("ST")
                        .referenceAfter("IED1.AP1.LD1.LN1.DO1")
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.reference().ldName.get());
        assertTrue(result.isFieldPresent("fc"));
        assertEquals("ST", result.fc().get());
        assertTrue(result.isFieldPresent("referenceAfter"));
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.referenceAfter().get());
    }

    @Test
    @DisplayName("REQUEST with lnReference instead of ldName")
    void requestWithLnReference() throws Exception {
        CmsGetAllDataValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetAllDataValues(MessageType.REQUEST)
                        .lnReference("IED1.AP1.LD1.LN1")
                        .fc("MX")
                        .reqId(2));

        assertEquals(2, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1", result.reference().lnReference.get());
        assertTrue(result.isFieldPresent("fc"));
        assertEquals("MX", result.fc().get());
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("REQUEST without optional fc and referenceAfter")
    void requestWithoutOptionals() throws Exception {
        CmsGetAllDataValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetAllDataValues(MessageType.REQUEST)
                        .ldName("IED1.AP1.LD1")
                        .reqId(3));

        assertEquals(3, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.reference().ldName.get());
        assertFalse(result.isFieldPresent("fc"));
        assertFalse(result.isFieldPresent("referenceAfter"));
        assertTrue(result.referenceAfter().get().isEmpty());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.RESPONSE_POSITIVE).reqId(4);
        asdu.data().add(new CmsDataEntry().reference("DO1").value(new CmsInt32(100)));
        asdu.data().add(new CmsDataEntry().reference("DO2").value(new CmsInt32(200)));
        asdu.moreFollows().set(true);

        CmsGetAllDataValues result = AsduTestUtil.roundTripViaApdu(asdu);

        assertEquals(4, result.reqId().get());
        assertEquals(2, result.data().size());
        assertEquals("DO1", result.data().get(0).reference().get());
        assertEquals("DO2", result.data().get(1).reference().get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: empty data list")
    void positiveResponseEmpty() throws Exception {
        CmsGetAllDataValues asdu = new CmsGetAllDataValues(MessageType.RESPONSE_POSITIVE).reqId(5);
        asdu.moreFollows().set(false);
        CmsGetAllDataValues result = AsduTestUtil.roundTripViaApdu(asdu);
        assertEquals(5, result.reqId().get());
        assertTrue(result.data().isEmpty());
        assertFalse(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetAllDataValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetAllDataValues(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                        .reqId(6));

        assertEquals(6, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU")
    void requestRoundTripAsduOnly() throws Exception {
        CmsGetAllDataValues result = AsduTestUtil.roundTripViaAsdu(
                new CmsGetAllDataValues(MessageType.REQUEST)
                        .ldName("IED1.AP1.LD1")
                        .fc("ST")
                        .referenceAfter("IED1.AP1.LD1.LN1.DO1")
                        .reqId(10));

        assertEquals(10, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.reference().ldName.get());
        assertTrue(result.isFieldPresent("fc"));
        assertEquals("ST", result.fc().get());
        assertTrue(result.isFieldPresent("referenceAfter"));
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.referenceAfter().get());
    }
}
