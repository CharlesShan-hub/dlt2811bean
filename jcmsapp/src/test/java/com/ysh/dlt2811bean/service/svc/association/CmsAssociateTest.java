package com.ysh.dlt2811bean.service.svc.association;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.transport.protocol.CmsFrameManager;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AuthenticationParameter;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.FromFlagsTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAssociate")
class CmsAssociateTest implements
        ServiceNameTest<CmsAssociate>,
        CopyTest<CmsAssociate>,
        FromFlagsTest<CmsAssociate> {

    private static final byte[] ASSOC_ID = new byte[]{
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17,
            0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F,
            0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27,
            0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F,
            0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,
            0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F
    };

    @Override
    public ServiceName expectedServiceName() {
        return ServiceName.ASSOCIATE;
    }

    @Override
    public CmsAssociate createAsdu() {
        return new CmsAssociate(MessageType.REQUEST);
    }

    @Override
    public CmsAssociate createCopyableAsdu() {
        return new CmsAssociate(MessageType.REQUEST)
                .serverAccessPointReference("IED1", "AP1")
                .reqId(10);
    }

    @Override
    public CmsAssociate createFromFlags(boolean resp, boolean err) {
        return new CmsAssociate(resp, err);
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsAssociate result = AsduTestUtil.roundTripViaApdu(
                new CmsAssociate(MessageType.REQUEST)
                        .serverAccessPointReference("IED1", "AP1")
                        .authenticationParameter(new AuthenticationParameter()
                                .signatureCertificate(new byte[]{0x01, 0x02, 0x03})
                                .signedTime(1715000000L, 1234567, 4)
                                .signedValue(new byte[]{0x0A, 0x0B}))
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1", result.serverAccessPointReference().get());
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03},
                result.authenticationParameter().signatureCertificate.get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsAssociate result = AsduTestUtil.roundTripViaApdu(
                new CmsAssociate(MessageType.RESPONSE_POSITIVE)
                        .associationId(ASSOC_ID)
                        .authenticationParameter(new AuthenticationParameter()
                                .signatureCertificate(new byte[]{0x10, 0x20})
                                .signedTime(1715000000L, 1234567, 4)
                                .signedValue(new byte[]{0x30, 0x40}))
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals(CmsServiceError.NO_ERROR, result.serviceError().get());
        assertArrayEquals(new byte[]{0x10, 0x20},
                result.authenticationParameter().signatureCertificate.get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsAssociate result = AsduTestUtil.roundTripViaApdu(
                new CmsAssociate(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
                        .reqId(1));

        assertEquals(1, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via ASDU")
    void requestRoundTripAsduOnly() throws Exception {
        CmsAssociate result = AsduTestUtil.roundTripViaAsdu(
                new CmsAssociate(MessageType.REQUEST)
                        .serverAccessPointReference("IED2", "AP2")
                        .reqId(5));

        assertEquals(5, result.reqId().get());
        assertEquals("IED2.AP2", result.serverAccessPointReference().get());
    }

    @Test
    @DisplayName("split and merge large Associate REQUEST")
    void splitAndMergeLargeAssociate() throws Exception {
        byte[] largeCert = new byte[40000];
        byte[] largeSign = new byte[30000];
        for (int i = 0; i < largeCert.length; i++) largeCert[i] = (byte) (i & 0xFF);
        for (int i = 0; i < largeSign.length; i++) largeSign[i] = (byte) ((i * 7) & 0xFF);

        CmsAssociate asdu = new CmsAssociate(MessageType.REQUEST)
                .serverAccessPointReference("IED1", "AP1")
                .authenticationParameter(new AuthenticationParameter()
                        .signatureCertificate(largeCert)
                        .signedTime(1715000000L, 1234567, 4)
                        .signedValue(largeSign))
                .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);
        List<CmsApdu> segments = CmsFrameManager.split(apdu);
        assertTrue(segments.size() >= 2, "Should produce at least 2 segments");

        PerOutputStream pos = new PerOutputStream();
        for (CmsApdu seg : segments) {
            seg.encode(pos);
        }
        byte[] encoded = pos.toByteArray();

        PerInputStream pis = new PerInputStream(encoded);
        List<CmsApdu> loaded = new ArrayList<>();
        CmsApdu seg;
        do {
            seg = new CmsApdu().load(pis);
            loaded.add(seg);
        } while (seg.getApch().isNext());

        CmsApdu merged = CmsFrameManager.merge(loaded);
        merged.decodeAsdu();

        CmsAssociate result = (CmsAssociate) merged.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1", result.serverAccessPointReference().get());
        assertArrayEquals(largeCert, result.authenticationParameter().signatureCertificate.get());
        assertArrayEquals(largeSign, result.authenticationParameter().signedValue.get());
    }
}
