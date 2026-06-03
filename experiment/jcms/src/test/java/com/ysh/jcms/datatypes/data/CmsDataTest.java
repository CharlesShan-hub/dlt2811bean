package com.ysh.jcms.datatypes.data;

import com.ysh.jcms.datatypes.compound.CmsBinaryTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsData")
class CmsDataTest {

    // ==================== Scalar types ====================

    @Test
    void booleanRoundtrip() {
        byte[] enc = CmsData.createBoolean(true).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.BOOLEAN, dec.choice());
        assertTrue(dec.boolVal());
    }

    @Test
    void int8Roundtrip() {
        byte[] enc = CmsData.createInt8((byte) -128).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.INT8, dec.choice());
        assertEquals(-128, dec.intVal());
    }

    @Test
    void int16Roundtrip() {
        byte[] enc = CmsData.createInt16((short) 32767).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.INT16, dec.choice());
        assertEquals(32767, dec.intVal());
    }

    @Test
    void int32Roundtrip() {
        byte[] enc = CmsData.createInt32(-1234567890).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.INT32, dec.choice());
        assertEquals(-1234567890, dec.intVal());
    }

    @Test
    void int64Roundtrip() {
        byte[] enc = CmsData.createInt64(Long.MIN_VALUE + 1).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.INT64, dec.choice());
        assertEquals(Long.MIN_VALUE + 1, dec.intVal());
    }

    @Test
    void int8uRoundtrip() {
        byte[] enc = CmsData.createInt8U((short) 255).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.INT8U, dec.choice());
        assertEquals(255, dec.intVal());
    }

    @Test
    void int16uRoundtrip() {
        byte[] enc = CmsData.createInt16U(65535).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.INT16U, dec.choice());
        assertEquals(65535, dec.intVal());
    }

    @Test
    void int32uRoundtrip() {
        byte[] enc = CmsData.createInt32U(4294967295L).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.INT32U, dec.choice());
        assertEquals(4294967295L, dec.intVal());
    }

    @Test
    void int64uRoundtrip() {
        byte[] enc = CmsData.createInt64U(-1).encode(); // max as unsigned = -1 signed
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.INT64U, dec.choice());
        assertEquals(-1, dec.intVal());
    }

    @Test
    void float32Roundtrip() {
        byte[] enc = CmsData.createFloat32(3.141592f).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.FLOAT32, dec.choice());
        assertEquals(3.141592f, dec.floatVal(), 1e-6f);
    }

    @Test
    void float64Roundtrip() {
        byte[] enc = CmsData.createFloat64(2.718281828459045).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.FLOAT64, dec.choice());
        assertEquals(2.718281828459045, dec.floatVal(), 1e-15);
    }

    // ==================== String types ====================

    @Test
    void visibleStringRoundtrip() {
        byte[] enc = CmsData.createVisibleString("Hello, World!").encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.VISIBLE_STRING, dec.choice());
        assertEquals("Hello, World!", dec.strVal());
    }

    @Test
    void utf8StringRoundtrip() {
        String s = "你好，世界！🌍";
        byte[] enc = CmsData.createUtf8String(s).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.UTF8_STRING, dec.choice());
        assertEquals(s, dec.strVal());
    }

    // ==================== Binary data types ====================

    @Test
    void octetStringRoundtrip() {
        byte[] data = {0x00, 0x01, 0x02, (byte) 0xFF, (byte) 0xFE};
        byte[] enc = CmsData.createOctetString(data).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.OCTET_STRING, dec.choice());
        assertArrayEquals(data, dec.bytesVal());
    }

    @Test
    void bitStringRoundtrip() {
        // 13 bits: 1010101010101
        byte[] data = {(byte) 0xAA, (byte) 0xA0};
        int nbits = 13;
        byte[] enc = CmsData.createBitString(data, nbits).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.BIT_STRING, dec.choice());
        assertEquals(nbits, dec.nbits());
        // Verify first nbits bits match
        byte[] decData = dec.bytesVal();
        int nbytes = (nbits + 7) / 8;
        for (int i = 0; i < nbytes; i++) {
            assertEquals(data[i], decData[i]);
        }
    }

    // ==================== Time types ====================

    @Test
    void utcTimeRoundtrip() {
        long ms = 1700000000000L; // 2023-11-14T22:13:20.000Z
        byte[] enc = CmsData.createUtcTime(ms).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.UTC_TIME, dec.choice());
        assertEquals(ms, dec.utcTimeMs());
    }

    @Test
    void binaryTimeRoundtrip() {
        CmsBinaryTime bt = new CmsBinaryTime(10, 30, 45, 500, 15000);
        byte[] enc = CmsData.createBinaryTime(bt).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.BINARY_TIME, dec.choice());
        assertEquals(bt.msOfDay, dec.binaryTimeVal().msOfDay);
        assertEquals(bt.daysSince1984, dec.binaryTimeVal().daysSince1984);
    }

    // ==================== Enumerated / bit-field types ====================

    @Test
    void qualityRoundtrip() {
        byte[] quality = {(byte) 0xAA, (byte) 0x18}; // bits: 10101010 00011 = 13 bits
        byte[] enc = CmsData.createQuality(quality).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.QUALITY, dec.choice());
        // Only first 13 bits survive; byte[1] retains only 5 MSBits
        assertEquals(quality[0], dec.quality()[0]);
        assertEquals(quality[1] & 0xF8, dec.quality()[1] & 0xF8);
    }

    @Test
    void dbposRoundtrip() {
        byte[] enc = CmsData.createDbpos(2).encode(); // DB_POS_ON
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.DBPOS, dec.choice());
        assertEquals(2, dec.dbpos());
    }

    @Test
    void tcmdRoundtrip() {
        byte[] enc = CmsData.createTcmd(2).encode(); // TCMD_OPERATE
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.TCMD, dec.choice());
        assertEquals(2, dec.tcmd());
    }

    @Test
    void checkRoundtrip() {
        byte[] check = {(byte) 0x80, (byte) 0x00};
        byte[] enc = CmsData.createCheck(check).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.CHECK, dec.choice());
        assertArrayEquals(check, dec.check());
    }

    @Test
    void errorRoundtrip() {
        byte[] enc = CmsData.createError(5).encode(); // PARAMETER_VALUE_INAPPROPRIATE
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.ERROR, dec.choice());
        assertEquals(5, dec.serviceError());
    }

    // ==================== Container types ====================

    @Test
    void arrayRoundtrip() {
        CmsArray arr = new CmsArray(Arrays.asList(
            CmsData.createBoolean(true),
            CmsData.createInt32(42),
            CmsData.createFloat64(3.14),
            CmsData.createVisibleString("test")
        ));
        byte[] enc = CmsData.createArray(arr).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.ARRAY, dec.choice());

        assertEquals(4, dec.arrayVal().size());
        assertTrue(dec.arrayVal().get(0).boolVal());
        assertEquals(42, dec.arrayVal().get(1).intVal());
        assertEquals(3.14, dec.arrayVal().get(2).floatVal(), 1e-15);
        assertEquals("test", dec.arrayVal().get(3).strVal());
    }

    @Test
    void structureRoundtrip() {
        CmsArray members = new CmsArray(Arrays.asList(
            CmsData.createInt8((byte) 10),
            CmsData.createInt16((short) 20),
            CmsData.createInt32(30)
        ));
        byte[] enc = CmsData.createStructure(members).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.STRUCTURE, dec.choice());

        assertEquals(3, dec.arrayVal().size());
        assertEquals(10, dec.arrayVal().get(0).intVal());
        assertEquals(20, dec.arrayVal().get(1).intVal());
        assertEquals(30, dec.arrayVal().get(2).intVal());
    }

    @Test
    void nestedArrayRoundtrip() {
        CmsArray inner = new CmsArray(Arrays.asList(
            CmsData.createBoolean(false),
            CmsData.createInt64(999L)
        ));
        CmsData outer = CmsData.createArray(new CmsArray(Arrays.asList(
            CmsData.createArray(inner),
            CmsData.createVisibleString("nested")
        )));
        byte[] enc = outer.encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.ARRAY, dec.choice());

        assertEquals(2, dec.arrayVal().size());

        // Inner array
        assertEquals(CmsData.ARRAY, dec.arrayVal().get(0).choice());
        assertEquals(2, dec.arrayVal().get(0).arrayVal().size());
        assertFalse(dec.arrayVal().get(0).arrayVal().get(0).boolVal());
        assertEquals(999L, dec.arrayVal().get(0).arrayVal().get(1).intVal());

        // Sibling
        assertEquals("nested", dec.arrayVal().get(1).strVal());
    }

    @Test
    void emptyArrayRoundtrip() {
        byte[] enc = CmsData.createArray(new CmsArray()).encode();
        CmsData dec = CmsData.decode(enc);
        assertEquals(CmsData.ARRAY, dec.choice());
        assertTrue(dec.arrayVal().elements().isEmpty());
    }

    @Test
    void wrongFieldReturnsDefault() {
        CmsData d = CmsData.createInt32(10);
        assertNull(d.strVal());
        assertFalse(d.boolVal());
        assertNull(d.arrayVal());
        assertEquals(0.0, d.floatVal(), 0.0);
    }
}