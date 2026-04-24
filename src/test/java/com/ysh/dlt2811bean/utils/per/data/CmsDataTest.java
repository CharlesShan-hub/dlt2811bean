package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.data.CmsInt32;
import com.ysh.dlt2811bean.utils.per.data.CmsServiceError;
import com.ysh.dlt2811bean.utils.per.data.CmsVisibleString;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

import com.ysh.dlt2811bean.utils.per.data.CmsStructure;

import com.ysh.dlt2811bean.utils.per.data.CmsArray;
import com.ysh.dlt2811bean.utils.per.data.CmsData;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsData")
class CmsDataTest {

    @Test
    @DisplayName("error entity [0]")
    void errorEntity() throws Exception {
        CmsData<CmsServiceError> data = new CmsData<CmsServiceError>()
                .set(new CmsServiceError(CmsServiceError.ACCESS_VIOLATION));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<?> decoded = new CmsData<>().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsServiceError.ACCESS_VIOLATION, (int) ((CmsServiceError) decoded.get()).get());
    }

    @Test
    @DisplayName("error static [0]")
    void errorStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsServiceError(CmsServiceError.ACCESS_VIOLATION));

        CmsServiceError res = new CmsServiceError();
        CmsData.read(new PerInputStream(pos.toByteArray()), res);
        assertEquals(CmsServiceError.ACCESS_VIOLATION, (int) res.get());
    }

    @Test
    @DisplayName("array entity [1]")
    void arrayEntity() throws Exception {
        CmsData<CmsArray<CmsInt32>> data = new CmsData<CmsArray<CmsInt32>>()
            .set(new CmsArray<>(CmsInt32.class).max(10)
                .add(new CmsInt32(100))
                .add(new CmsInt32(200))
                .add(new CmsInt32(300))
            );
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsArray<CmsInt32> decodedArr = new CmsData<CmsArray<CmsInt32>>()
            .set(new CmsArray<>(CmsInt32.class).max(10))
            .decode(new PerInputStream(pos.toByteArray()))
            .get();
        assertEquals(3, decodedArr.size());
        assertEquals(100, (int) ((CmsInt32) decodedArr.get(0)).get());
        assertEquals(200, (int) ((CmsInt32) decodedArr.get(1)).get());
        assertEquals(300, (int) ((CmsInt32) decodedArr.get(2)).get());
    }

    @Test
    @DisplayName("array static [1]")
    void arrayStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsArray<>(CmsInt32.class).max(10)
            .add(new CmsInt32(100))
            .add(new CmsInt32(200))
            .add(new CmsInt32(300)));

        CmsArray<CmsInt32> template = new CmsArray<>(CmsInt32.class).max(10);
        CmsData.read(new PerInputStream(pos.toByteArray()), template);
        assertEquals(3, template.size());
        assertEquals(100, (int) ((CmsInt32) template.get(0)).get());
        assertEquals(200, (int) ((CmsInt32) template.get(1)).get());
        assertEquals(300, (int) ((CmsInt32) template.get(2)).get());
    }

    @Test
    @DisplayName("structure entity [2]")
    void structureEntity() throws Exception {
        CmsData<CmsStructure> data = new CmsData<CmsStructure>()
            .set(new CmsStructure()
                .max(10)
                .add(new CmsData<CmsInt32>().set(new CmsInt32(42)))
                .add(new CmsData<CmsVisibleString>().set(new CmsVisibleString("hello").max(10)))
            );
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsStructure template = new CmsStructure().max(10)
            .add(new CmsData<CmsInt32>())
            .add(new CmsData<CmsVisibleString>().set(new CmsVisibleString("").max(10)));

        CmsStructure decodedStruct = new CmsData<CmsStructure>()
            .set(template)
            .decode(new PerInputStream(pos.toByteArray()))
            .get();

        assertEquals(2, decodedStruct.size());
        assertEquals(42, (int) ((CmsInt32) ((CmsData<?>) decodedStruct.get(0)).get()).get());
        assertEquals("hello", ((CmsVisibleString) ((CmsData<?>) decodedStruct.get(1)).get()).get());
    }

    @Test
    @DisplayName("structure static [2]")
    void structureStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsStructure().max(10)
            .add(new CmsData<CmsInt32>().set(new CmsInt32(42)))
            .add(new CmsData<CmsVisibleString>().set(new CmsVisibleString("hello").max(10))));

        CmsStructure template = new CmsStructure().max(10)
            .add(new CmsData<CmsInt32>())
            .add(new CmsData<CmsVisibleString>().set(new CmsVisibleString("").max(10)));
        CmsData.read(new PerInputStream(pos.toByteArray()), template);

        assertEquals(2, template.size());
        assertEquals(42, (int) ((CmsInt32) ((CmsData<?>) template.get(0)).get()).get());
        assertEquals("hello", ((CmsVisibleString) ((CmsData<?>) template.get(1)).get()).get());
    }
//
    @Test
    @DisplayName("boolean entity [3]")
    void booleanEntity() throws Exception {
        CmsData<CmsBoolean> data = new CmsData<CmsBoolean>()
                .set(new CmsBoolean().set(true));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<?> decoded = new CmsData<>().decode(new PerInputStream(pos.toByteArray()));
        assertTrue(((CmsBoolean) decoded.get()).get());
    }

    @Test
    @DisplayName("boolean static [3]")
    void booleanStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsBoolean().set(true));

        CmsBoolean res = new CmsBoolean();
        CmsData.read(new PerInputStream(pos.toByteArray()), res);
        assertTrue(res.get());
    }
//
//    @Test
//    @DisplayName("int8 [4]")
//    void int8() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 4;
//        data.int8Value.set(-42);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(4, decoded.choiceIndex);
//        assertEquals(-42, (int) decoded.int8Value.get());
//    }
//
//    @Test
//    @DisplayName("int16 [5]")
//    void int16() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 5;
//        data.int16Value.set(-1000);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(5, decoded.choiceIndex);
//        assertEquals(-1000, (int) decoded.int16Value.get());
//    }
//
//    @Test
//    @DisplayName("int32 [6]")
//    void int32() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 6;
//        data.int32Value.set(100000);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(6, decoded.choiceIndex);
//        assertEquals(100000, (int) decoded.int32Value.get());
//    }
//
//    @Test
//    @DisplayName("int64 [7]")
//    void int64() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 7;
//        data.int64Value.set(1000000000000L);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(7, decoded.choiceIndex);
//        assertEquals(1000000000000L, (long) decoded.int64Value.get());
//    }
//
//    @Test
//    @DisplayName("int8u [8]")
//    void int8u() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 8;
//        data.int8uValue.set(200);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(8, decoded.choiceIndex);
//        assertEquals(200, (int) decoded.int8uValue.get());
//    }
//
//    @Test
//    @DisplayName("int16u [9]")
//    void int16u() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 9;
//        data.int16uValue.set(50000);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(9, decoded.choiceIndex);
//        assertEquals(50000, (int) decoded.int16uValue.get());
//    }
//
//    @Test
//    @DisplayName("int32u [10]")
//    void int32u() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 10;
//        data.int32uValue.set(3000000000L);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(10, decoded.choiceIndex);
//        assertEquals(3000000000L, (long) decoded.int32uValue.get());
//    }
//
//    @Test
//    @DisplayName("int64u [11]")
//    void int64u() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 11;
//        data.int64uValue.set(new java.math.BigInteger("12345678901234567890"));
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(11, decoded.choiceIndex);
//        assertEquals(new java.math.BigInteger("12345678901234567890"), decoded.int64uValue.get());
//    }
//
//    @Test
//    @DisplayName("float32 [12]")
//    void float32() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 12;
//        data.float32Value.set(3.14f);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(12, decoded.choiceIndex);
//        assertEquals(3.14f, decoded.float32Value.get(), 0.001f);
//    }
//
//    @Test
//    @DisplayName("float64 [13]")
//    void float64() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 13;
//        data.float64Value.set(220.5);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(13, decoded.choiceIndex);
//        assertEquals(220.5, decoded.float64Value.get(), 0.001);
//    }
//
//    @Test
//    @DisplayName("bit-string [14]")
//    void bitString() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 14;
//        data.bitStringValue.set(new byte[]{ (byte) 0xAA }, 8);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(14, decoded.choiceIndex);
//        assertArrayEquals(new byte[]{ (byte) 0xAA }, decoded.bitStringValue.get());
//    }
//
//    @Test
//    @DisplayName("octet-string [15]")
//    void octetString() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 15;
//        data.octetStringValue.set(new byte[]{ 0x01, 0x02, 0x03 });
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(15, decoded.choiceIndex);
//        assertArrayEquals(new byte[]{ 0x01, 0x02, 0x03 }, decoded.octetStringValue.get());
//    }
//
//    @Test
//    @DisplayName("visible-string [16]")
//    void visibleString() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 16;
//        data.visibleStringValue.set("hello");
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(16, decoded.choiceIndex);
//        assertEquals("hello", decoded.visibleStringValue.get());
//    }
//
//    @Test
//    @DisplayName("unicode-string [17]")
//    void unicodeString() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 17;
//        data.unicodeStringValue.set("你好");
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(17, decoded.choiceIndex);
//        assertEquals("你好", decoded.unicodeStringValue.get());
//    }
//
//    @Test
//    @DisplayName("utc-time [18]")
//    void utcTime() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 18;
//        data.utcTimeValue.secondsSinceEpoch.set(1715000000L);
//        data.utcTimeValue.fractionOfSecond.set(1234567);
//        data.utcTimeValue.timeQuality.set(0x20);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(18, decoded.choiceIndex);
//        assertEquals(1715000000L, (long) decoded.utcTimeValue.secondsSinceEpoch.get());
//        assertEquals(1234567, (int) decoded.utcTimeValue.fractionOfSecond.get());
//    }
//
//    @Test
//    @DisplayName("binary-time [19]")
//    void binaryTime() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 19;
//        data.binaryTimeValue.msOfDay.set(43200000L);
//        data.binaryTimeValue.daysSince1984.set(15000);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(19, decoded.choiceIndex);
//        assertEquals(43200000L, (long) decoded.binaryTimeValue.msOfDay.get());
//        assertEquals(15000, (int) decoded.binaryTimeValue.daysSince1984.get());
//    }
//
//    @Test
//    @DisplayName("quality [20]")
//    void quality() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 20;
//        data.qualityValue.setValidity(CmsQuality.INVALID);
//        data.qualityValue.setBit(CmsQuality.OVERFLOW, true);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(20, decoded.choiceIndex);
//        assertEquals(CmsQuality.INVALID, decoded.qualityValue.getValidity());
//        assertTrue(decoded.qualityValue.testBit(CmsQuality.OVERFLOW));
//    }
//
//    @Test
//    @DisplayName("dbpos [21]")
//    void dbpos() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 21;
//        data.dbposValue.set(CmsDbpos.ON);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(21, decoded.choiceIndex);
//        assertTrue(decoded.dbposValue.is(CmsDbpos.ON));
//    }
//
//    @Test
//    @DisplayName("tcmd [22]")
//    void tcmd() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 22;
//        data.tcmdValue.set(CmsTcmd.HIGHER);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(22, decoded.choiceIndex);
//        assertTrue(decoded.tcmdValue.is(CmsTcmd.HIGHER));
//    }
//
//    @Test
//    @DisplayName("check [23]")
//    void check() throws Exception {
//        CmsData data = new CmsData();
//        data.choiceIndex = 23;
//        data.checkValue.setBit(CmsCheck.SYNCHROCHECK, true);
//        data.checkValue.setBit(CmsCheck.INTERLOCK_CHECK, true);
//
//        PerOutputStream pos = new PerOutputStream();
//        data.encode(pos);
//
//        CmsData decoded = new CmsData().decode(new PerInputStream(pos.toByteArray()));
//        assertEquals(23, decoded.choiceIndex);
//        assertTrue(decoded.checkValue.testBit(CmsCheck.SYNCHROCHECK));
//        assertTrue(decoded.checkValue.testBit(CmsCheck.INTERLOCK_CHECK));
//    }
//
//    @Test
//    @DisplayName("encode without choiceIndex throws exception")
//    void encodeWithoutChoiceIndex() {
//        CmsData data = new CmsData();
//        PerOutputStream pos = new PerOutputStream();
//        assertThrows(IllegalStateException.class, () -> data.encode(pos));
//    }
//
//    @Test
//    @DisplayName("toString unset")
//    void toStringUnset() {
//        assertEquals("Data[unset]", new CmsData().toString());
//    }
//
//    @Test
//    @DisplayName("toString with choice")
//    void toStringWithChoice() {
//        CmsData data = new CmsData();
//        data.choiceIndex = 6;
//        data.int32Value.set(100);
//        assertEquals("Data[6]=INT32: 100", data.toString());
//    }
}