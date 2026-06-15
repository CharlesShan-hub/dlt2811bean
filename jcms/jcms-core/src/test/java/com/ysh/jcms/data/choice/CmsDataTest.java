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

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_boolean() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_BOOLEAN);
        a.alt_boolean.value(true);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_int8() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT8);
        a.alt_int8.value(-42);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_int16() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT16);
        a.alt_int16.value(-30000);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_int32() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT32);
        a.alt_int32.value(-2000000000);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_int64() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT64);
        a.alt_int64.value(-9000000000000000000L);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_int8u() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT8U);
        a.alt_int8u.value(200);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_int16u() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT16U);
        a.alt_int16u.value(60000);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_int32u() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT32U);
        a.alt_int32u.value(3000000000L);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_int64u() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT64U);
        a.alt_int64u.value(new BigInteger("10000000000000000"));

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_float32() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_FLOAT32);
        a.alt_float32.value(3.14f);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_float64() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_FLOAT64);
        a.alt_float64.value(2.718281828459045);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
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

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_octet_string() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_OCTET_STRING);
        a.alt_octet_string.value("Hello".getBytes());

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_visible_string() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_VISIBLE_STRING);
        a.alt_visible_string.value("ABC-123".getBytes());

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_unicode_string() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_UNICODE_STRING);
        a.alt_unicode_string.value("你好世界".getBytes());

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
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

        assertEquals(a, b);
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

        assertEquals(a, b);
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

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_dbpos() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_DBPOS);
        a.alt_dbpos.value(CmsDbpos.ON);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundtrip_tcmd() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_TCMD);
        a.alt_tcmd.value(CmsTcmd.OPERATE);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
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

        assertEquals(a, b);
    }
}
