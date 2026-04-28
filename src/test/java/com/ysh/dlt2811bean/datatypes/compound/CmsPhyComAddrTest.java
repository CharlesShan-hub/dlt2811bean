package com.ysh.dlt2811bean.datatypes.compound;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt8U;
import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsPhyComAddr")
class CmsPhyComAddrTest {

    @Test
    @DisplayName("default constructor initializes all fields to zero")
    void default_isAllZero() {
        CmsPhyComAddr addr = new CmsPhyComAddr();
        assertEquals(0, addr.addr.get().length);
        assertEquals(0, addr.priority.get());
        assertEquals(0, addr.vid.get());
        assertEquals(0, addr.appid.get());
    }

    @Test
    @DisplayName("full constructor with raw values")
    void fullConstructor_withRawValues() {
        byte[] mac = {(byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, (byte) 0xEE, (byte) 0xFF};
        CmsPhyComAddr addr = new CmsPhyComAddr(mac, 4, 100, 0x0001);
        assertArrayEquals(mac, addr.addr.get());
        assertEquals(4, addr.priority.get());
        assertEquals(100, addr.vid.get());
        assertEquals(0x0001, addr.appid.get());
    }

    @Test
    @DisplayName("chain setters via public fields")
    void setters_chain() {
        byte[] mac = {(byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        CmsPhyComAddr addr = new CmsPhyComAddr();
        addr.addr.set(mac);
        addr.priority.set(4);
        addr.vid.set(100);
        addr.appid.set(0x0001);
        assertArrayEquals(mac, addr.addr.get());
        assertEquals(4, addr.priority.get());
        assertEquals(100, addr.vid.get());
        assertEquals(0x0001, addr.appid.get());
    }

    @Test
    @DisplayName("chain setters via public fields")
    void setters_fields() {
        byte[] mac = {(byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        CmsPhyComAddr addr = new CmsPhyComAddr();
        addr.addr.set(mac);
        addr.priority.set(4);
        addr.vid.set(100);
        addr.appid.set(0x0001);
        assertArrayEquals(mac, addr.addr.get());
        assertEquals(4, addr.priority.get());
        assertEquals(100, addr.vid.get());
        assertEquals(0x0001, addr.appid.get());
    }

    @Test
    @DisplayName("encode and decode roundtrip")
    void encodeDecode_roundtrip() throws Exception {
        byte[] mac = {(byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        CmsPhyComAddr addr = new CmsPhyComAddr(mac, 4, 100, 0x0001);

        PerOutputStream pos = new PerOutputStream();
        addr.encode(pos);

        CmsPhyComAddr result = new CmsPhyComAddr().decode(new PerInputStream(pos.toByteArray()));

        assertArrayEquals(mac, result.addr.get());
        assertEquals(4, result.priority.get());
        assertEquals(100, result.vid.get());
        assertEquals(0x0001, result.appid.get());
    }

    @Test
    @DisplayName("encode and decode zeros")
    void encodeDecode_zeros() throws Exception {
        CmsPhyComAddr addr = new CmsPhyComAddr(new byte[6], 0, 0, 0);

        PerOutputStream pos = new PerOutputStream();
        addr.encode(pos);

        CmsPhyComAddr result = new CmsPhyComAddr().decode(new PerInputStream(pos.toByteArray()));

        assertArrayEquals(new byte[6], result.addr.get());
        assertEquals(0, result.priority.get());
        assertEquals(0, result.vid.get());
        assertEquals(0, result.appid.get());
    }

    @Test
    @DisplayName("encode and decode max values")
    void encodeDecode_maxValues() throws Exception {
        byte[] maxAddr = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        CmsPhyComAddr addr = new CmsPhyComAddr(maxAddr, 255, 65535, 65535);

        PerOutputStream pos = new PerOutputStream();
        addr.encode(pos);

        CmsPhyComAddr result = new CmsPhyComAddr().decode(new PerInputStream(pos.toByteArray()));

        assertArrayEquals(maxAddr, result.addr.get());
        assertEquals(255, result.priority.get());
        assertEquals(65535, result.vid.get());
        assertEquals(65535, result.appid.get());
    }

    @Test
    @DisplayName("encode produces exactly 11 bytes (6 + 1 + 2 + 2)")
    void encode_is11Bytes() {
        CmsPhyComAddr addr = new CmsPhyComAddr(new byte[6], 0, 0, 0);

        PerOutputStream pos = new PerOutputStream();
        addr.encode(pos);

        assertEquals(11, pos.toByteArray().length);
    }

    @Test
    @DisplayName("encode and decode roundtrip via instance methods")
    void encodeDecode_instance() throws Exception {
        byte[] mac = {(byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        CmsPhyComAddr addr = new CmsPhyComAddr(mac, 4, 100, 0x0001);

        PerOutputStream pos = new PerOutputStream();
        addr.encode(pos);

        CmsPhyComAddr result = new CmsPhyComAddr().decode(new PerInputStream(pos.toByteArray()));

        assertArrayEquals(mac, result.addr.get());
        assertEquals(4, result.priority.get());
        assertEquals(100, result.vid.get());
        assertEquals(0x0001, result.appid.get());
    }

    @Test
    @DisplayName("toString contains all fields")
    void toString_containsAllFields() {
        byte[] mac = {(byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        CmsPhyComAddr addr = new CmsPhyComAddr(mac, 4, 100, 0x0001);
        String str = addr.toString();
        assertTrue(str.contains("addr:"));
        assertTrue(str.contains("priority:"));
        assertTrue(str.contains("vid:"));
        assertTrue(str.contains("appid:"));
    }

    @Test
    @DisplayName("convenience setters with raw values")
    void setters_convenience() {
        byte[] mac = {(byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        CmsPhyComAddr addr = new CmsPhyComAddr()
                .addr(mac)
                .priority(4)
                .vid(100)
                .appid(0x0001);
        assertArrayEquals(mac, addr.addr.get());
        assertEquals(4, addr.priority.get());
        assertEquals(100, addr.vid.get());
        assertEquals(0x0001, addr.appid.get());
    }
}