package com.ysh.dlt2811bean.datatypes.data;

import com.ysh.dlt2811bean.datatypes.code.CmsCheck;
import com.ysh.dlt2811bean.datatypes.code.CmsQuality;
import com.ysh.dlt2811bean.datatypes.code.CmsTimeQuality;
import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.datatypes.compound.CmsBinaryTime;
import com.ysh.dlt2811bean.datatypes.compound.CmsUtcTime;
import com.ysh.dlt2811bean.datatypes.code.CmsDbpos;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.code.CmsTcmd;
import com.ysh.dlt2811bean.datatypes.numeric.*;
import com.ysh.dlt2811bean.datatypes.string.CmsBitString;
import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import com.ysh.dlt2811bean.datatypes.string.CmsUtf8String;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

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
        assertEquals(CmsServiceError.ACCESS_VIOLATION, res.get());
    }

    @Test
    @DisplayName("array entity [1]")
    void arrayEntity() throws Exception {
        CmsData<CmsArray<CmsInt32>> data = new CmsData<CmsArray<CmsInt32>>()
            .set(new CmsArray<>(CmsInt32::new).capacity(10)
                .add(new CmsInt32(100))
                .add(new CmsInt32(200))
                .add(new CmsInt32(300))
            );
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsArray<CmsInt32> decodedArr = new CmsData<CmsArray<CmsInt32>>()
            .set(new CmsArray<>(CmsInt32::new).capacity(10))
            .decode(new PerInputStream(pos.toByteArray()))
            .get();
        assertEquals(3, decodedArr.size());
        assertEquals(100, decodedArr.get(0).get());
        assertEquals(200, decodedArr.get(1).get());
        assertEquals(300, decodedArr.get(2).get());
    }

    @Test
    @DisplayName("array static [1]")
    void arrayStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsArray<>(CmsInt32::new).capacity(10)
            .add(new CmsInt32(100))
            .add(new CmsInt32(200))
            .add(new CmsInt32(300)));

        CmsArray<CmsInt32> template = new CmsArray<>(CmsInt32::new).capacity(10);
        CmsData.read(new PerInputStream(pos.toByteArray()), template);
        assertEquals(3, template.size());
        assertEquals(100, template.get(0).get());
        assertEquals(200, template.get(1).get());
        assertEquals(300, template.get(2).get());
    }

    @Test
    @DisplayName("structure entity [2]")
    void structureEntity() throws Exception {
        CmsData<CmsStructure> data = new CmsData<CmsStructure>()
            .set(new CmsStructure()
                .capacity(10)
                .add(new CmsInt32(42))
                .add(new CmsVisibleString("hello").max(10))
            );
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsStructure template = new CmsStructure().capacity(10)
            .add(new CmsInt32())
            .add(new CmsVisibleString("").max(10));

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
        CmsData.write(pos, new CmsStructure().capacity(10)
                .add(new CmsInt32(42))
                .add(new CmsVisibleString("hello").max(10)));

        CmsStructure template = new CmsStructure().capacity(10)
                .add(new CmsInt32())
                .add(new CmsVisibleString("").max(10));
        CmsData.read(new PerInputStream(pos.toByteArray()), template);

        assertEquals(2, template.size());
        assertEquals(42, ((CmsInt32) ((CmsData<?>) template.get(0)).get()).get());
        assertEquals("hello", ((CmsVisibleString) ((CmsData<?>) template.get(1)).get()).get());
    }

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

    @Test
    @DisplayName("int8 entity [4]")
    void int8Entity() throws Exception {
        CmsData<CmsInt8> data = new CmsData<CmsInt8>()
                .set(new CmsInt8().set(-42));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<?> decoded = new CmsData<>().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(-42, (int) ((CmsInt8) decoded.get()).get());
    }

    @Test
    @DisplayName("int8 static [4]")
    void int8Static() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsInt8().set(-42));

        CmsInt8 res = new CmsInt8();
        CmsData.read(new PerInputStream(pos.toByteArray()), res);
        assertEquals(-42, res.get());
    }

    @Test
    @DisplayName("int16 entity [5]")
    void int16Entity() throws Exception {
        CmsData<CmsInt16> data = new CmsData<CmsInt16>()
                .set(new CmsInt16().set(-1000));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<?> decoded = new CmsData<>().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(-1000, (int) ((CmsInt16) decoded.get()).get());
    }

    @Test
    @DisplayName("int16 static [5]")
    void int16Static() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsInt16().set(-1000));

        CmsInt16 res = new CmsInt16();
        CmsData.read(new PerInputStream(pos.toByteArray()), res);
        assertEquals(-1000, res.get());
    }

    @Test
    @DisplayName("int32 entity [6]")
    void int32Entity() throws Exception {
        CmsData<CmsInt32> data = new CmsData<CmsInt32>()
                .set(new CmsInt32().set(100000));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<?> decoded = new CmsData<>().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(100000, (int) ((CmsInt32) decoded.get()).get());
    }

    @Test
    @DisplayName("int32 static [6]")
    void int32Static() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsInt32().set(100000));

        CmsInt32 res = new CmsInt32();
        CmsData.read(new PerInputStream(pos.toByteArray()), res);
        assertEquals(100000, res.get());
    }

    @Test
    @DisplayName("int64 entity [7]")
    void int64Entity() throws Exception {
        CmsData<CmsInt64> data = new CmsData<CmsInt64>()
                .set(new CmsInt64().set(1000000000000L));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<?> decoded = new CmsData<>().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(1000000000000L, (long) ((CmsInt64) decoded.get()).get());
    }

    @Test
    @DisplayName("int64 static [7]")
    void int64Static() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsInt64().set(1000000000000L));

        CmsInt64 res = new CmsInt64();
        CmsData.read(new PerInputStream(pos.toByteArray()), res);
        assertEquals(1000000000000L, res.get());
    }

    @Test
    @DisplayName("int8u entity [8]")
    void int8uEntity() throws Exception {
        CmsData<CmsInt8U> data = new CmsData<CmsInt8U>()
                .set(new CmsInt8U().set(200));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<?> decoded = new CmsData<>().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(200, (int) ((CmsInt8U) decoded.get()).get());
    }

    @Test
    @DisplayName("int8u static [8]")
    void int8uStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsInt8U().set(200));

        CmsInt8U res = new CmsInt8U();
        CmsData.read(new PerInputStream(pos.toByteArray()), res);
        assertEquals(200, res.get());
    }

    @Test
    @DisplayName("int16u entity [9]")
    void int16uEntity() throws Exception {
        CmsData<CmsInt16U> data = new CmsData<CmsInt16U>()
                .set(new CmsInt16U().set(50000));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<?> decoded = new CmsData<>().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(50000, (int) ((CmsInt16U) decoded.get()).get());
    }

    @Test
    @DisplayName("int16u static [9]")
    void int16uStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsInt16U().set(50000));

        CmsInt16U res = new CmsInt16U();
        CmsData.read(new PerInputStream(pos.toByteArray()), res);
        assertEquals(50000, res.get());
    }

    @Test
    @DisplayName("int32u entity [10]")
    void int32uEntity() throws Exception {
        CmsData<CmsInt32U> data = new CmsData<CmsInt32U>()
                .set(new CmsInt32U().set(3000000000L));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<?> decoded = new CmsData<>().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(3000000000L, (long) ((CmsInt32U) decoded.get()).get());
    }

    @Test
    @DisplayName("int32u static [10]")
    void int32uStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsInt32U().set(3000000000L));

        CmsInt32U res = new CmsInt32U();
        CmsData.read(new PerInputStream(pos.toByteArray()), res);
        assertEquals(3000000000L, res.get());
    }

    @Test
    @DisplayName("int64u entity [11]")
    void int64uEntity() throws Exception {
        CmsData<CmsInt64U> data = new CmsData<CmsInt64U>()
                .set(new CmsInt64U().set(new java.math.BigInteger("12345678901234567890")));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<?> decoded = new CmsData<>().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(new java.math.BigInteger("12345678901234567890"), ((CmsInt64U) decoded.get()).get());
    }

    @Test
    @DisplayName("int64u static [11]")
    void int64uStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsInt64U().set(new java.math.BigInteger("12345678901234567890")));

        CmsInt64U res = new CmsInt64U();
        CmsData.read(new PerInputStream(pos.toByteArray()), res);
        assertEquals(new java.math.BigInteger("12345678901234567890"), res.get());
    }

    @Test
    @DisplayName("float32 entity [12]")
    void float32Entity() throws Exception {
        CmsData<CmsFloat32> data = new CmsData<CmsFloat32>()
                .set(new CmsFloat32().set(3.14f));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<?> decoded = new CmsData<>().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(3.14f, ((CmsFloat32) decoded.get()).get(), 0.001f);
    }

    @Test
    @DisplayName("float32 static [12]")
    void float32Static() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsFloat32().set(3.14f));

        CmsFloat32 res = new CmsFloat32();
        CmsData.read(new PerInputStream(pos.toByteArray()), res);
        assertEquals(3.14f, res.get(), 0.001f);
    }

    @Test
    @DisplayName("float64 entity [13]")
    void float64Entity() throws Exception {
        CmsData<CmsFloat64> data = new CmsData<CmsFloat64>()
                .set(new CmsFloat64().set(220.5));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<?> decoded = new CmsData<>().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(220.5, ((CmsFloat64) decoded.get()).get(), 0.001);
    }

    @Test
    @DisplayName("float64 static [13]")
    void float64Static() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsFloat64().set(220.5));

        CmsFloat64 res = new CmsFloat64();
        CmsData.read(new PerInputStream(pos.toByteArray()), res);
        assertEquals(220.5, res.get(), 0.001);
    }
//
    @Test
    @DisplayName("bit-string entity [14]")
    void bitStringEntity() throws Exception {
        CmsData<CmsBitString> data = new CmsData<CmsBitString>()
                .set(new CmsBitString(0b10101010L, 8).size(8));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<CmsBitString> decoded = new CmsData<CmsBitString>()
                .set(new CmsBitString().size(8))
                .decode(new PerInputStream(pos.toByteArray()));
        assertEquals(0b10101010L, decoded.get().getLongValue());
    }

    @Test
    @DisplayName("bit-string static [14]")
    void bitStringStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsBitString(0b10101010L, 8).size(8));

        CmsBitString template = new CmsBitString().size(8);
        CmsData.read(new PerInputStream(pos.toByteArray()), template);
        assertEquals(0b10101010L, template.getLongValue());
    }

    @Test
    @DisplayName("octet-string entity [15]")
    void octetStringEntity() throws Exception {
        CmsData<CmsOctetString> data = new CmsData<CmsOctetString>()
                .set(new CmsOctetString(new byte[]{0x01, 0x02, 0x03}).max(10));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<CmsOctetString> decoded = new CmsData<CmsOctetString>()
                .set(new CmsOctetString().max(10))
                .decode(new PerInputStream(pos.toByteArray()));
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, decoded.get().get());
    }

    @Test
    @DisplayName("octet-string static [15]")
    void octetStringStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsOctetString(new byte[]{0x01, 0x02, 0x03}).max(10));

        CmsOctetString template = new CmsOctetString().max(10);
        CmsData.read(new PerInputStream(pos.toByteArray()), template);
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, template.get());
    }

    @Test
    @DisplayName("visible-string entity [16]")
    void visibleStringEntity() throws Exception {
        CmsData<CmsVisibleString> data = new CmsData<CmsVisibleString>()
                .set(new CmsVisibleString("hello").max(10));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<CmsVisibleString> decoded = new CmsData<CmsVisibleString>()
                .set(new CmsVisibleString().max(10))
                .decode(new PerInputStream(pos.toByteArray()));
        assertEquals("hello", decoded.get().get());
    }

    @Test
    @DisplayName("visible-string static [16]")
    void visibleStringStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsVisibleString("hello").max(10));

        CmsVisibleString template = new CmsVisibleString().max(10);
        CmsData.read(new PerInputStream(pos.toByteArray()), template);
        assertEquals("hello", template.get());
    }

    @Test
    @DisplayName("unicode-string entity [17]")
    void unicodeStringEntity() throws Exception {
        CmsData<CmsUtf8String> data = new CmsData<CmsUtf8String>()
                .set(new CmsUtf8String("设备").max(10));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<CmsUtf8String> decoded = new CmsData<CmsUtf8String>()
                .set(new CmsUtf8String().max(10))
                .decode(new PerInputStream(pos.toByteArray()));
        assertEquals("设备", decoded.get().get());
    }

    @Test
    @DisplayName("unicode-string static [17]")
    void unicodeStringStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsUtf8String("设备").max(10));

        CmsUtf8String template = new CmsUtf8String().max(10);
        CmsData.read(new PerInputStream(pos.toByteArray()), template);
        assertEquals("设备", template.get());
    }

    @Test
    @DisplayName("utc-time entity [18]")
    void utcTimeEntity() throws Exception {
        CmsData<CmsUtcTime> data = new CmsData<CmsUtcTime>()
                .set(new CmsUtcTime()
                        .secondsSinceEpoch(1715000000L)
                        .fractionOfSecond(1234567)
                        .timeQuality(new CmsTimeQuality(0x20)));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<CmsUtcTime> decoded = new CmsData<CmsUtcTime>()
                .set(new CmsUtcTime())
                .decode(new PerInputStream(pos.toByteArray()));
        assertEquals(1715000000L, decoded.get().secondsSinceEpoch.get());
        assertEquals(1234567, decoded.get().fractionOfSecond.get());
        assertEquals(0x20, decoded.get().timeQuality.get());
    }

    @Test
    @DisplayName("utc-time static [18]")
    void utcTimeStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsUtcTime()
                .secondsSinceEpoch(1715000000L)
                .fractionOfSecond(1234567)
                .timeQuality(new CmsTimeQuality(0x20)));

        CmsUtcTime template = new CmsUtcTime();
        CmsData.read(new PerInputStream(pos.toByteArray()), template);
        assertEquals(1715000000L, template.secondsSinceEpoch.get());
        assertEquals(1234567, template.fractionOfSecond.get());
        assertEquals(0x20, template.timeQuality.get());
    }

    @Test
    @DisplayName("binary-time entity [19]")
    void binaryTimeEntity() throws Exception {
        CmsData<CmsBinaryTime> data = new CmsData<CmsBinaryTime>()
                .set(new CmsBinaryTime()
                        .msOfDay(43200000L)
                        .daysSince1984(15000));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<CmsBinaryTime> decoded = new CmsData<CmsBinaryTime>()
                .set(new CmsBinaryTime())
                .decode(new PerInputStream(pos.toByteArray()));
        assertEquals(43200000L, decoded.get().msOfDay.get());
        assertEquals(15000, decoded.get().daysSince1984.get());
    }

    @Test
    @DisplayName("binary-time static [19]")
    void binaryTimeStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsBinaryTime()
                .msOfDay(43200000L)
                .daysSince1984(15000));

        CmsBinaryTime template = new CmsBinaryTime();
        CmsData.read(new PerInputStream(pos.toByteArray()), template);
        assertEquals(43200000L, template.msOfDay.get());
        assertEquals(15000, template.daysSince1984.get());
    }

    @Test
    @DisplayName("quality entity [20]")
    void qualityEntity() throws Exception {
        CmsData<CmsQuality> data = new CmsData<CmsQuality>()
                .set(new CmsQuality()
                        .setValidity(CmsQuality.INVALID)
                        .setBit(CmsQuality.OVERFLOW, true));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<CmsQuality> decoded = new CmsData<CmsQuality>()
                .set(new CmsQuality())
                .decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsQuality.INVALID, decoded.get().getValidity());
        assertTrue(decoded.get().testBit(CmsQuality.OVERFLOW));
    }

    @Test
    @DisplayName("quality static [20]")
    void qualityStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsQuality()
                .setValidity(CmsQuality.INVALID)
                .setBit(CmsQuality.OVERFLOW, true));

        CmsQuality template = new CmsQuality();
        CmsData.read(new PerInputStream(pos.toByteArray()), template);
        assertEquals(CmsQuality.INVALID, template.getValidity());
        assertTrue(template.testBit(CmsQuality.OVERFLOW));
    }

    @Test
    @DisplayName("dbpos entity [21]")
    void dbposEntity() throws Exception {
        CmsData<CmsDbpos> data = new CmsData<CmsDbpos>()
                .set(new CmsDbpos(CmsDbpos.ON));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<CmsDbpos> decoded = new CmsData<CmsDbpos>()
                .set(new CmsDbpos())
                .decode(new PerInputStream(pos.toByteArray()));
        assertEquals((long) CmsDbpos.ON, decoded.get().get());
    }

    @Test
    @DisplayName("dbpos static [21]")
    void dbposStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsDbpos(CmsDbpos.ON));

        CmsDbpos template = new CmsDbpos();
        CmsData.read(new PerInputStream(pos.toByteArray()), template);
        assertEquals((long) CmsDbpos.ON, template.get());
    }

    @Test
    @DisplayName("tcmd entity [22]")
    void tcmdEntity() throws Exception {
        CmsData<CmsTcmd> data = new CmsData<CmsTcmd>()
                .set(new CmsTcmd(CmsTcmd.HIGHER));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<CmsTcmd> decoded = new CmsData<CmsTcmd>()
                .set(new CmsTcmd())
                .decode(new PerInputStream(pos.toByteArray()));
        assertEquals((long) CmsTcmd.HIGHER, decoded.get().get());
    }

    @Test
    @DisplayName("tcmd static [22]")
    void tcmdStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsTcmd(CmsTcmd.HIGHER));

        CmsTcmd template = new CmsTcmd();
        CmsData.read(new PerInputStream(pos.toByteArray()), template);
        assertEquals((long) CmsTcmd.HIGHER, template.get());
    }

    @Test
    @DisplayName("check entity [23]")
    void checkEntity() throws Exception {
        CmsData<CmsCheck> data = new CmsData<CmsCheck>()
                .set(new CmsCheck()
                        .setBit(CmsCheck.SYNCHROCHECK, true)
                        .setBit(CmsCheck.INTERLOCK_CHECK, true));
        PerOutputStream pos = new PerOutputStream();
        data.encode(pos);

        CmsData<CmsCheck> decoded = new CmsData<CmsCheck>()
                .set(new CmsCheck())
                .decode(new PerInputStream(pos.toByteArray()));
        assertTrue(decoded.get().testBit(CmsCheck.SYNCHROCHECK));
        assertTrue(decoded.get().testBit(CmsCheck.INTERLOCK_CHECK));
    }

    @Test
    @DisplayName("check static [23]")
    void checkStatic() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsData.write(pos, new CmsCheck()
                .setBit(CmsCheck.SYNCHROCHECK, true)
                .setBit(CmsCheck.INTERLOCK_CHECK, true));

        CmsCheck template = new CmsCheck();
        CmsData.read(new PerInputStream(pos.toByteArray()), template);
        assertTrue(template.testBit(CmsCheck.SYNCHROCHECK));
        assertTrue(template.testBit(CmsCheck.INTERLOCK_CHECK));
    }

    @Test
    @DisplayName("encode without value throws exception")
    void encodeWithoutValue() {
        CmsData<?> data = new CmsData<>();
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalStateException.class, () -> data.encode(pos));
    }

    @Test
    @DisplayName("toString unset")
    void toStringUnset() {
        assertEquals("Data[unset]", new CmsData<>().toString());
    }

    @Test
    @DisplayName("toString with value")
    void toStringWithValue() {
        CmsData<CmsInt32> data = new CmsData<CmsInt32>()
                .set(new CmsInt32(100));
        assertEquals("Data[6]=(CmsInt32) 100", data.toString());
    }

    @Test
    @DisplayName("copy with value")
    void copyWithValue() {
        CmsData<CmsInt32> original = new CmsData<CmsInt32>()
                .set(new CmsInt32(100));
        CmsData<CmsInt32> cloned = original.copy();
        assertEquals(100, (int) cloned.get().get());
        assertNotSame(original, cloned);
        assertNotSame(original.get(), cloned.get());
    }

    @Test
    @DisplayName("copy with null value")
    void copyWithNullValue() {
        CmsData<?> original = new CmsData<>();
        CmsData<?> cloned = original.copy();
        assertNull(cloned.get());
        assertNotSame(original, cloned);
    }

    @Test
    @DisplayName("copy is deep")
    void copyIsDeep() {
        CmsData<CmsInt32> original = new CmsData<CmsInt32>()
                .set(new CmsInt32(100));
        CmsData<CmsInt32> cloned = original.copy();
        cloned.get().set(999);
        assertEquals(100, (int) original.get().get());
    }
}
