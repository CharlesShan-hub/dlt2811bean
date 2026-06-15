package com.ysh.dlt2811bean.per.types;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PerUtf8String")
class PerUtf8StringTest {

    // ==================== UTF-8 ====================

    @Test
    @DisplayName("UTF-8: ASCII string")
    void utf8_ascii() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerUtf8String.encodeUtf8(pos, "device name");

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("device name", PerUtf8String.decodeUtf8(pis));
    }

    @Test
    @DisplayName("UTF-8: Chinese characters")
    void utf8_chinese() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerUtf8String.encodeUtf8(pos, "设备名称");

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("设备名称", PerUtf8String.decodeUtf8(pis));
    }

    @Test
    @DisplayName("UTF-8: empty string")
    void utf8_empty() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerUtf8String.encodeUtf8(pos, "");

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("", PerUtf8String.decodeUtf8(pis));
    }

    @Test
    @DisplayName("UTF-8: null string")
    void utf8_null() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerUtf8String.encodeUtf8(pos, null);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("", PerUtf8String.decodeUtf8(pis));
    }

    @Test
    @DisplayName("UTF-8 constrained: with byte range")
    void utf8_constrained() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerUtf8String.encodeUtf8Constrained(pos, "abc", 0, 255);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("abc", PerUtf8String.decodeUtf8Constrained(pis, 0, 255));
    }

    // ==================== BMP ====================

    @Test
    @DisplayName("BMP fixed-size: ASCII")
    void bmpFixed_ascii() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerUtf8String.encodeBmpFixedSize(pos, "AB", 2);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("AB", PerUtf8String.decodeBmpFixedSize(pis, 2));
    }

    @Test
    @DisplayName("BMP fixed-size: padded with spaces")
    void bmpFixed_padded() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerUtf8String.encodeBmpFixedSize(pos, "A", 3);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("A", PerUtf8String.decodeBmpFixedSize(pis, 3));
    }

    @Test
    @DisplayName("BMP fixed-size: 0 chars")
    void bmpFixed_0() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerUtf8String.encodeBmpFixedSize(pos, "", 0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("", PerUtf8String.decodeBmpFixedSize(pis, 0));
    }

    @Test
    @DisplayName("BMP constrained: variable-length")
    void bmpConstrained() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerUtf8String.encodeBmpConstrained(pos, "test", 0, 100);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("test", PerUtf8String.decodeBmpConstrained(pis, 0, 100));
    }
}
