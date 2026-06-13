package com.ysh.jcms.data.choice;

import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.control.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.data.time.*;

import java.math.BigInteger;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsDataTest {

    @Test
    public void roundtrip_error() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_ERROR);
        a.alt_error.value(CmsServiceError.ACCESS_VIOLATION);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_ERROR, b.choice.value());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, b.alt_error.value());
    }

    @Test
    public void roundtrip_boolean() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_BOOLEAN);
        a.alt_boolean.value(true);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_BOOLEAN, b.choice.value());
        assertTrue(b.alt_boolean.value());
    }

    @Test
    public void roundtrip_int8() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT8);
        a.alt_int8.value(-42);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_INT8, b.choice.value());
        assertEquals(-42, b.alt_int8.value());
    }

    @Test
    public void roundtrip_int16() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT16);
        a.alt_int16.value(-30000);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_INT16, b.choice.value());
        assertEquals(-30000, b.alt_int16.value());
    }

    @Test
    public void roundtrip_int32() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT32);
        a.alt_int32.value(-2000000000);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_INT32, b.choice.value());
        assertEquals(-2000000000, b.alt_int32.value());
    }

    @Test
    public void roundtrip_int64() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT64);
        a.alt_int64.value(-9000000000000000000L);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_INT64, b.choice.value());
        assertEquals(-9000000000000000000L, b.alt_int64.value());
    }

    @Test
    public void roundtrip_int8u() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT8U);
        a.alt_int8u.value(200);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_INT8U, b.choice.value());
        assertEquals(200, b.alt_int8u.value());
    }

    @Test
    public void roundtrip_int16u() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT16U);
        a.alt_int16u.value(60000);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_INT16U, b.choice.value());
        assertEquals(60000, b.alt_int16u.value());
    }

    @Test
    public void roundtrip_int32u() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT32U);
        a.alt_int32u.value(3000000000L);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_INT32U, b.choice.value());
        assertEquals(3000000000L, b.alt_int32u.value());
    }

    @Test
    public void roundtrip_int64u() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT64U);
        a.alt_int64u.value(new BigInteger("10000000000000000"));

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_INT64U, b.choice.value());
        assertEquals(new BigInteger("10000000000000000"), b.alt_int64u.value());
    }

    @Test
    public void roundtrip_float32() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_FLOAT32);
        a.alt_float32.value(3.14f);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_FLOAT32, b.choice.value());
        assertEquals(3.14f, b.alt_float32.value(), 1e-6f);
    }

    @Test
    public void roundtrip_float64() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_FLOAT64);
        a.alt_float64.value(2.718281828459045);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_FLOAT64, b.choice.value());
        assertEquals(2.718281828459045, b.alt_float64.value(), 1e-12);
    }

    @Test
    public void roundtrip_bit_string() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_BIT_STRING);
        byte[] raw = {(byte)0xAA, (byte)0xBB};
        a.alt_bit_string.value(raw);
        a.alt_bit_string.len = 16;  /* 2 bytes × 8 = 16 bits */
        a.alt_bit_string.write();

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_BIT_STRING, b.choice.value());
        assertEquals(16, b.alt_bit_string.len);
        assertArrayEquals(raw, b.alt_bit_string.value.getByteArray(0, 2));
    }

    @Test
    public void roundtrip_octet_string() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_OCTET_STRING);
        a.alt_octet_string.value("Hello".getBytes());

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_OCTET_STRING, b.choice.value());
        assertArrayEquals("Hello".getBytes(), b.alt_octet_string.value());
    }

    @Test
    public void roundtrip_visible_string() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_VISIBLE_STRING);
        a.alt_visible_string.value("ABC-123".getBytes());

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_VISIBLE_STRING, b.choice.value());
        assertArrayEquals("ABC-123".getBytes(), b.alt_visible_string.value());
    }

    @Test
    public void roundtrip_unicode_string() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_UNICODE_STRING);
        a.alt_unicode_string.value("你好世界".getBytes());

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_UNICODE_STRING, b.choice.value());
        assertArrayEquals("你好世界".getBytes(), b.alt_unicode_string.value());
    }

    @Test
    public void roundtrip_utc_time() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_UTC_TIME);
        a.alt_utc_time.seconds_since_epoch.value(1234567890L);
        a.alt_utc_time.fraction_of_second.value(500000);
        a.alt_utc_time.time_quality.leap_seconds_known.value(true);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_UTC_TIME, b.choice.value());
        assertEquals(1234567890L, b.alt_utc_time.seconds_since_epoch.value());
        assertEquals(500000, b.alt_utc_time.fraction_of_second.value());
        assertTrue(b.alt_utc_time.time_quality.leap_seconds_known.value());
    }

    @Test
    public void roundtrip_binary_time() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_BINARY_TIME);
        a.alt_binary_time.msOfDay.value(43200000L);
        a.alt_binary_time.daysSince1984.value(5000);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_BINARY_TIME, b.choice.value());
        assertEquals(43200000L, b.alt_binary_time.msOfDay.value());
        assertEquals(5000, b.alt_binary_time.daysSince1984.value());
    }

    @Test
    public void roundtrip_quality() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_QUALITY);
        a.alt_quality.validity.value(1);
        a.alt_quality.overflow.value(true);
        a.alt_quality.failure.value(true);
        a.alt_quality.inaccurate.value(true);
        a.alt_quality.outOfRange.value(true);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_QUALITY, b.choice.value());
        assertEquals(1, b.alt_quality.validity.value());
        assertTrue(b.alt_quality.overflow.value());
        assertTrue(b.alt_quality.failure.value());
        assertTrue(b.alt_quality.inaccurate.value());
        assertTrue(b.alt_quality.outOfRange.value());
    }

    @Test
    public void roundtrip_dbpos() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_DBPOS);
        a.alt_dbpos.value(CmsDbpos.ON);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_DBPOS, b.choice.value());
        assertEquals(CmsDbpos.ON, b.alt_dbpos.value());
    }

    @Test
    public void roundtrip_tcmd() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_TCMD);
        a.alt_tcmd.value(CmsTcmd.OPERATE);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_TCMD, b.choice.value());
        assertEquals(CmsTcmd.OPERATE, b.alt_tcmd.value());
    }

    @Test
    public void roundtrip_check() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_CHECK);
        a.alt_check.syncheck.value(true);
        a.alt_check.interlock_check.value(false);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(CmsData.CHOICE_CHECK, b.choice.value());
        assertTrue(b.alt_check.syncheck.value());
        assertFalse(b.alt_check.interlock_check.value());
    }
}
