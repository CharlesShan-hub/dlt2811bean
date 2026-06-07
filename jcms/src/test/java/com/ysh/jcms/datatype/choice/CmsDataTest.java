package com.ysh.jcms.datatype.choice;

import com.ysh.jcms.datatype.common.CmsServiceError;
import com.ysh.jcms.datatype.common.CmsDbpos;
import com.ysh.jcms.datatype.common.CmsTcmd;
import com.ysh.jcms.datatype.common.CmsQuality;
import static com.ysh.jcms.datatype.choice.CmsDataType.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsData")
class CmsDataTest {

    // ====== Simple types ======

    @Test
    void type0Error() {
        CmsData original = new CmsData();
        original.choice().value(ERROR);
        original.value.error.value(CmsServiceError.INSTANCE_NOT_AVAILABLE);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals(ERROR, decoded.choice().value());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, decoded.value.error.value());
    }

    @Test
    void type3BooleanTrue() {
        CmsData original = new CmsData();
        original.choice().value(BOOLEAN);
        original.value.boolean_value.value(true);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals(BOOLEAN, decoded.choice().value());
        assertTrue(decoded.value.boolean_value.value());
    }

    @Test
    void type3BooleanFalse() {
        CmsData original = new CmsData();
        original.choice().value(BOOLEAN);
        original.value.boolean_value.value(false);

        CmsData decoded = new CmsData().decode(original.encode());
        assertFalse(decoded.value.boolean_value.value());
    }

    @Test
    void type4Int8() {
        CmsData original = new CmsData();
        original.choice().value(INT8);
        original.value.int8.value((byte) -42);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals((byte) -42, decoded.value.int8.value());
    }

    @Test
    void type5Int16() {
        CmsData original = new CmsData();
        original.choice().value(INT16);
        original.value.int16.value((short) -20000);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals((short) -20000, decoded.value.int16.value());
    }

    @Test
    void type6Int32() {
        CmsData original = new CmsData();
        original.choice().value(INT32);
        original.value.int32.value(-12345);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals(-12345, decoded.value.int32.value());
    }

    @Test
    void type7Int64() {
        CmsData original = new CmsData();
        original.choice().value(INT64);
        original.value.int64.value(-9876543210L);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals(-9876543210L, decoded.value.int64.value());
    }

    @Test
    void type8Int8U() {
        CmsData original = new CmsData();
        original.choice().value(INT8U);
        original.value.int8u.value((byte) 200);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals((byte) 200, decoded.value.int8u.value());
    }

    @Test
    void type9Int16U() {
        CmsData original = new CmsData();
        original.choice().value(INT16U);
        original.value.int16u.value((short) 60000);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals((short) 60000, decoded.value.int16u.value());
    }

    @Test
    void type10Int32U() {
        CmsData original = new CmsData();
        original.choice().value(INT32U);
        original.value.int32u.value(999999);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals(999999, decoded.value.int32u.value());
    }

    @Test
    void type12Float32() {
        CmsData original = new CmsData();
        original.choice().value(FLOAT32);
        original.value.float32.value(3.14f);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals(3.14f, decoded.value.float32.value(), 1e-6f);
    }

    @Test
    void type13Float64() {
        CmsData original = new CmsData();
        original.choice().value(FLOAT64);
        original.value.float64.value(2.71828);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals(2.71828, decoded.value.float64.value(), 1e-10);
    }

    @Test
    void type18UtcTime() {
        CmsData original = new CmsData();
        original.choice().value(UTC_TIME);
        original.value.utc_time.set(1718015445500L);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals(UTC_TIME, decoded.choice().value());
        assertEquals(original.value.utc_time.seconds_since_epoch().value(),
                     decoded.value.utc_time.seconds_since_epoch().value());
    }

    @Test
    void type19BinaryTime() {
        CmsData original = new CmsData();
        original.choice().value(BINARY_TIME);
        original.value.binary_time.set(1718015445500L);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals(BINARY_TIME, decoded.choice().value());
        assertEquals(original.value.binary_time.msOfDay().value(),
                     decoded.value.binary_time.msOfDay().value());
    }

    @Test
    void type20Quality() {
        CmsData original = new CmsData();
        original.choice().value(QUALITY);
        original.value.quality.overflow().value(true);
        original.value.quality.failure().value(true);

        CmsData decoded = new CmsData().decode(original.encode());
        assertTrue(decoded.value.quality.overflow().value());
        assertTrue(decoded.value.quality.failure().value());
    }

    @Test
    void type21Dbpos() {
        CmsData original = new CmsData();
        original.choice().value(DBPOS);
        original.value.dbpos.value(CmsDbpos.ON);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals(CmsDbpos.ON, decoded.value.dbpos.value());
    }

    @Test
    void type22Tcmd() {
        CmsData original = new CmsData();
        original.choice().value(TCMD);
        original.value.tcmd.value(CmsTcmd.SELECT);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals(CmsTcmd.SELECT, decoded.value.tcmd.value());
    }

    @Test
    void type23Check() {
        CmsData original = new CmsData();
        original.choice().value(CHECK);
        original.value.check.syncheck().value(true);
        original.value.check.interlock_check().value(false);

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals(CHECK, decoded.choice().value());
        assertTrue(decoded.value.check.syncheck().value());
        assertFalse(decoded.value.check.interlock_check().value());
    }

    @Test
    void defaultChoiceIsZero() {
        CmsData data = new CmsData();
        assertEquals(0, data.choice().value());
    }

    // ====== Factory helpers ======

    @Test
    void factoryOfInt32() {
        CmsData d = CmsData.of(INT32, -12345);
        assertEquals(INT32, d.choice().value());
        assertEquals(-12345, d.value.int32.value());
    }

    @Test
    void factoryOfBoolean() {
        CmsData d = CmsData.of(BOOLEAN, true);
        assertTrue(d.value.boolean_value.value());
    }

    // ====== Array type (choice=1) ======

    @Test
    void arrayOfTwoInt32EncodeDecode() {
        CmsData original = CmsData.array(
            CmsData.of(INT32, 42),
            CmsData.of(INT32, 99));

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals(ARRAY, decoded.choice().value());
        assertEquals(2, decoded.value.array.count);

        CmsData[] elems = decoded.elements();
        assertEquals(2, elems.length);
        assertEquals(INT32, elems[0].choice().value());
        assertEquals(42, elems[0].value.int32.value());
        assertEquals(99, elems[1].value.int32.value());
    }

    @Test
    void arrayOfBooleanAndInt32() {
        CmsData original = CmsData.array(
            CmsData.of(BOOLEAN, true),
            CmsData.of(INT32, 777));

        CmsData decoded = new CmsData().decode(original.encode());
        CmsData[] elems = decoded.elements();

        assertEquals(2, elems.length);
        assertEquals(BOOLEAN, elems[0].choice().value());
        assertTrue(elems[0].value.boolean_value.value());
        assertEquals(INT32, elems[1].choice().value());
        assertEquals(777, elems[1].value.int32.value());
    }

    // ====== Structure type (choice=2) ======

    @Test
    void structureOfOneInt32() {
        CmsData original = CmsData.structure(
            CmsData.of(INT32, 42));

        CmsData decoded = new CmsData().decode(original.encode());
        assertEquals(STRUCTURE, decoded.choice().value());
        assertEquals(1, decoded.value.structure.count);

        CmsData[] elems = decoded.elements();
        assertEquals(1, elems.length);
        assertEquals(42, elems[0].value.int32.value());
    }

    @Test
    void structureOfBooleanAndInt32() {
        CmsData original = CmsData.structure(
            CmsData.of(BOOLEAN, true),
            CmsData.of(INT32, 777));

        CmsData decoded = new CmsData().decode(original.encode());
        CmsData[] elems = decoded.elements();

        assertEquals(2, elems.length);
        assertEquals(BOOLEAN, elems[0].choice().value());
        assertTrue(elems[0].value.boolean_value.value());
        assertEquals(INT32, elems[1].choice().value());
        assertEquals(777, elems[1].value.int32.value());
    }
}
