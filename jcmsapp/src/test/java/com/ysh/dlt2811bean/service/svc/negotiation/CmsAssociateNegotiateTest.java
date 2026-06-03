package com.ysh.dlt2811bean.service.svc.negotiation;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.FromFlagsTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAssociateNegotiate")
class CmsAssociateNegotiateTest implements
        ServiceNameTest<CmsAssociateNegotiate>,
        CopyTest<CmsAssociateNegotiate>,
        FromFlagsTest<CmsAssociateNegotiate> {

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.ASSOCIATE_NEGOTIATE;
    }

    @Override
    public CmsAssociateNegotiate createAsdu() {
        return new CmsAssociateNegotiate(MessageType.REQUEST);
    }

    @Override
    public CmsAssociateNegotiate createCopyableAsdu() {
        return new CmsAssociateNegotiate(MessageType.REQUEST)
                .apduSize(4096).asduSize(65535L).protocolVersion(1L).reqId(10);
    }

    @Override
    public CmsAssociateNegotiate createFromFlags(boolean resp, boolean err) {
        return new CmsAssociateNegotiate(resp, err);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsAssociateNegotiate result = AsduTestUtil.roundTripViaApdu(
                new CmsAssociateNegotiate(MessageType.REQUEST)
                        .apduSize(4096).asduSize(65535L).protocolVersion(1L).reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals(4096, result.apduSize().get());
        assertEquals(65535L, result.asduSize().get());
        assertEquals(1L, result.protocolVersion().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsAssociateNegotiate result = AsduTestUtil.roundTripViaApdu(
                new CmsAssociateNegotiate(MessageType.RESPONSE_POSITIVE)
                        .apduSize(4096).asduSize(65535L).protocolVersion(1L).modelVersion("1.0").reqId(2));

        assertEquals(2, result.reqId().get());
        assertEquals(4096, result.apduSize().get());
        assertEquals(65535L, result.asduSize().get());
        assertEquals(1L, result.protocolVersion().get());
        assertEquals("1.0", result.modelVersion().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsAssociateNegotiate result = AsduTestUtil.roundTripViaApdu(
                new CmsAssociateNegotiate(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).reqId(3));

        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU")
    void requestRoundTripAsduOnly() throws Exception {
        CmsAssociateNegotiate result = AsduTestUtil.roundTripViaAsdu(
                new CmsAssociateNegotiate(MessageType.REQUEST)
                        .apduSize(2048).asduSize(10000L).protocolVersion(2L).reqId(10));

        assertEquals(10, result.reqId().get());
        assertEquals(2048, result.apduSize().get());
        assertEquals(10000L, result.asduSize().get());
        assertEquals(2L, result.protocolVersion().get());
    }

    @Test
    @DisplayName("toString for REQUEST")
    void toStringRequest() {
        CmsAssociateNegotiate asdu = new CmsAssociateNegotiate(MessageType.REQUEST)
                .apduSize(4096).asduSize(65535L).protocolVersion(1L).reqId(1);
        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsAssociateNegotiate) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }

    @Test
    @DisplayName("toString for RESPONSE_NEGATIVE")
    void toStringNegative() {
        CmsAssociateNegotiate asdu = new CmsAssociateNegotiate(MessageType.RESPONSE_NEGATIVE)
                .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).reqId(2);
        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsAssociateNegotiate) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 2"));
        assertTrue(str.contains("serviceError: (CmsServiceError) 1"));
    }
}
