package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsApch")
class CmsApchTest {

    @Test
    @DisplayName("encode and decode a request APCH")
    void requestRoundTrip() throws Exception {
        CmsApch apch = new CmsApch()
            .withServiceCode(ServiceCode.ASSOCIATE)
            .withMessageType(MessageType.REQUEST)
            .withFragmented(false)
            .withFrameLength(100);

        PerOutputStream pos = new PerOutputStream();
        apch.encode(pos);

        CmsApch decoded = new CmsApch();
        decoded.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(ServiceCode.ASSOCIATE, decoded.getServiceCode());
        assertEquals(MessageType.REQUEST, decoded.getMessageType());
        assertFalse(decoded.isFragmented());
        assertEquals(100, decoded.getFrameLength());
    }

    @Test
    @DisplayName("encode and decode a positive response APCH")
    void positiveResponseRoundTrip() throws Exception {
        CmsApch apch = new CmsApch()
            .withServiceCode(ServiceCode.GET_DATA_VALUES)
            .withMessageType(MessageType.RESPONSE_POSITIVE)
            .withFragmented(false)
            .withFrameLength(50);

        PerOutputStream pos = new PerOutputStream();
        apch.encode(pos);

        CmsApch decoded = new CmsApch();
        decoded.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(ServiceCode.GET_DATA_VALUES, decoded.getServiceCode());
        assertEquals(MessageType.RESPONSE_POSITIVE, decoded.getMessageType());
        assertFalse(decoded.isFragmented());
        assertEquals(50, decoded.getFrameLength());
    }

    @Test
    @DisplayName("encode and decode a negative response APCH")
    void negativeResponseRoundTrip() throws Exception {
        CmsApch apch = new CmsApch()
            .withServiceCode(ServiceCode.ASSOCIATE)
            .withMessageType(MessageType.RESPONSE_NEGATIVE)
            .withFragmented(false)
            .withFrameLength(10);

        PerOutputStream pos = new PerOutputStream();
        apch.encode(pos);

        CmsApch decoded = new CmsApch();
        decoded.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(ServiceCode.ASSOCIATE, decoded.getServiceCode());
        assertEquals(MessageType.RESPONSE_NEGATIVE, decoded.getMessageType());
        assertFalse(decoded.isFragmented());
        assertEquals(10, decoded.getFrameLength());
    }

    @Test
    @DisplayName("encode and decode with fragmented flag")
    void fragmentedRoundTrip() throws Exception {
        CmsApch apch = new CmsApch()
            .withServiceCode(ServiceCode.GET_DATA_VALUES)
            .withMessageType(MessageType.REQUEST)
            .withFragmented(true)
            .withFrameLength(200);

        PerOutputStream pos = new PerOutputStream();
        apch.encode(pos);

        CmsApch decoded = new CmsApch();
        decoded.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(ServiceCode.GET_DATA_VALUES, decoded.getServiceCode());
        assertEquals(MessageType.REQUEST, decoded.getMessageType());
        assertTrue(decoded.isFragmented());
        assertEquals(200, decoded.getFrameLength());
    }

    @Test
    @DisplayName("frame length zero")
    void frameLengthZero() throws Exception {
        CmsApch apch = new CmsApch()
            .withServiceCode(ServiceCode.TEST)
            .withMessageType(MessageType.REQUEST)
            .withFragmented(false)
            .withFrameLength(0);

        PerOutputStream pos = new PerOutputStream();
        apch.encode(pos);

        CmsApch decoded = new CmsApch();
        decoded.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(ServiceCode.TEST, decoded.getServiceCode());
        assertEquals(0, decoded.getFrameLength());
    }

    @Test
    @DisplayName("frame length maximum")
    void frameLengthMax() throws Exception {
        CmsApch apch = new CmsApch()
            .withServiceCode(ServiceCode.ASSOCIATE)
            .withMessageType(MessageType.REQUEST)
            .withFragmented(false)
            .withFrameLength(65535);

        PerOutputStream pos = new PerOutputStream();
        apch.encode(pos);

        CmsApch decoded = new CmsApch();
        decoded.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(65535, decoded.getFrameLength());
    }

    @Test
    @DisplayName("APCH frame is exactly 5 bytes")
    void frameSize() throws Exception {
        CmsApch apch = new CmsApch()
            .withServiceCode(ServiceCode.RELEASE)
            .withMessageType(MessageType.REQUEST)
            .withFragmented(false)
            .withFrameLength(0);

        PerOutputStream pos = new PerOutputStream();
        apch.encode(pos);

        byte[] bytes = pos.toByteArray();
        assertEquals(5, bytes.length);
    }

    @Test
    @DisplayName("PI is always 0x01")
    void piIsAlways0x01() throws Exception {
        CmsApch apch = new CmsApch()
            .withServiceCode(ServiceCode.ASSOCIATE)
            .withMessageType(MessageType.REQUEST)
            .withFragmented(false)
            .withFrameLength(0);

        PerOutputStream pos = new PerOutputStream();
        apch.encode(pos);

        byte[] bytes = pos.toByteArray();
        assertEquals(0x01, bytes[0] & 0xFF);
    }

    @Test
    @DisplayName("decode invalid PI throws exception")
    void invalidPi() {
        byte[] frame = new byte[]{0x02, 0x01, 0x00, 0x00, 0x00};
        CmsApch apch = new CmsApch();
        assertThrows(IllegalStateException.class, () -> apch.decode(new PerInputStream(frame)));
    }

    @Test
    @DisplayName("copy produces independent instance")
    void copy() throws Exception {
        CmsApch apch = new CmsApch()
            .withServiceCode(ServiceCode.ASSOCIATE)
            .withMessageType(MessageType.REQUEST)
            .withFragmented(true)
            .withFrameLength(100);

        CmsApch copy = apch.copy();

        assertEquals(apch.getServiceCode(), copy.getServiceCode());
        assertEquals(apch.getMessageType(), copy.getMessageType());
        assertEquals(apch.isFragmented(), copy.isFragmented());
        assertEquals(apch.getFrameLength(), copy.getFrameLength());

        // modify original, copy should be unaffected
        apch.withFrameLength(200);
        assertEquals(100, copy.getFrameLength());
    }

}
