package com.ysh.jcms.data.choice;

import com.ysh.jcms.data.common.*;
import java.math.BigInteger;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsDataTest {

    @Test
    public void roundup_error() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_ERROR;
        a.alt_error.value(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.choice, b.choice);
    }

    @Test
    public void roundup_boolean() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_BOOLEAN;
        a.alt_boolean.value(true);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.choice, b.choice);
        assertEquals(a.alt_boolean.value(), b.alt_boolean.value());
    }

    @Test
    public void roundup_int8() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_INT8;
        a.alt_int8.value(-42);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_int8.value(), b.alt_int8.value());
    }

    @Test
    public void roundup_int16() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_INT16;
        a.alt_int16.value(-30000);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_int16.value(), b.alt_int16.value());
    }

    @Test
    public void roundup_int32() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_INT32;
        a.alt_int32.value(-2000000000);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_int32.value(), b.alt_int32.value());
    }

    @Test
    public void roundup_int64() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_INT64;
        a.alt_int64.value(-9000000000000000000L);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_int64.value(), b.alt_int64.value());
    }

    @Test
    public void roundup_int8u() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_INT8U;
        a.alt_int8u.value(200);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_int8u.value(), b.alt_int8u.value());
    }

    @Test
    public void roundup_int16u() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_INT16U;
        a.alt_int16u.value(60000);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_int16u.value(), b.alt_int16u.value());
    }

    @Test
    public void roundup_int32u() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_INT32U;
        a.alt_int32u.value(3000000000L);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_int32u.value(), b.alt_int32u.value());
    }

    @Test
    public void roundup_int64u() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_INT64U;
        a.alt_int64u.value(new BigInteger("10000000000000000"));
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_int64u.value(), b.alt_int64u.value());
    }

    @Test
    public void roundup_float32() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_FLOAT32;
        a.alt_float32.value(3.14f);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_float32.value(), b.alt_float32.value(), 1e-6f);
    }

    @Test
    public void roundup_float64() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_FLOAT64;
        a.alt_float64.value(2.718281828459045);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_float64.value(), b.alt_float64.value(), 1e-10);
    }

    @Test
    public void roundup_bit_string() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_BIT_STRING;
        a.alt_bit_string = new byte[]{(byte) 0xAA, (byte) 0xBB};
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertArrayEquals(a.alt_bit_string, b.alt_bit_string);
    }

    @Test
    public void roundup_octet_string() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_OCTET_STRING;
        a.alt_octet_string = "Hello".getBytes();
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertArrayEquals(a.alt_octet_string, b.alt_octet_string);
    }

    @Test
    public void roundup_visible_string() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_VISIBLE_STRING;
        a.alt_visible_string = "ABC-123";
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_visible_string, b.alt_visible_string);
    }

    @Test
    public void roundup_unicode_string() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_UNICODE_STRING;
        a.alt_unicode_string = "你好世界";
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_unicode_string, b.alt_unicode_string);
    }

    @Test
    public void roundup_utc_time() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_UTC_TIME;
        a.alt_utc_time.secondsSinceEpoch.value(1234567890L);
        a.alt_utc_time.fractionOfSecond.value(500000);
        a.alt_utc_time.timeQuality.leap_seconds_known(true);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_utc_time.secondsSinceEpoch.value(), b.alt_utc_time.secondsSinceEpoch.value());
    }

    @Test
    public void roundup_binary_time() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_BINARY_TIME;
        a.alt_binary_time.msOfDay.value(43200000L);
        a.alt_binary_time.daysSince1984.value(5000);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_binary_time.msOfDay.value(), b.alt_binary_time.msOfDay.value());
    }

    @Test
    public void roundup_quality() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_QUALITY;
        a.alt_quality.validity(CmsQuality.INVALID);
        a.alt_quality.overflow(true);
        a.alt_quality.failure(true);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_quality.validity(), b.alt_quality.validity());
    }

    @Test
    public void roundup_dbpos() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_DBPOS;
        a.alt_dbpos.value(CmsDbpos.ON);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_dbpos.value(), b.alt_dbpos.value());
    }

    @Test
    public void roundup_tcmd() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_TCMD;
        a.alt_tcmd.value(CmsTcmd.OPERATE);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_tcmd.value(), b.alt_tcmd.value());
    }

    @Test
    public void roundup_check() {
        CmsData a = new CmsData();
        a.choice = CmsData.CHOICE_CHECK;
        a.alt_check.syncheck(true);
        a.alt_check.interlock_check(false);
        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);
        assertEquals(a.alt_check.syncheck(), b.alt_check.syncheck());
    }

    @Test
    public void roundup_array_of_data() {
        CmsData outer = new CmsData();
        outer.choice = CmsData.CHOICE_ARRAY;

        CmsData d1 = new CmsData();
        d1.choice = CmsData.CHOICE_INT32;
        d1.alt_int32.value(12345);

        CmsData d2 = new CmsData();
        d2.choice = CmsData.CHOICE_BOOLEAN;
        d2.alt_boolean.value(true);

        CmsData d3 = new CmsData();
        d3.choice = CmsData.CHOICE_FLOAT64;
        d3.alt_float64.value(3.14159);

        outer.alt_sequence.add(d1);
        outer.alt_sequence.add(d2);
        outer.alt_sequence.add(d3);

        byte[] encoded = outer.encode();
        CmsData decoded = new CmsData();
        decoded.decode(encoded);

        assertEquals(3, decoded.alt_sequence.size());
        assertEquals(CmsData.CHOICE_INT32, decoded.alt_sequence.get(0).choice);
        assertEquals(12345, decoded.alt_sequence.get(0).alt_int32.value());
        assertEquals(CmsData.CHOICE_BOOLEAN, decoded.alt_sequence.get(1).choice);
        assertEquals(true, decoded.alt_sequence.get(1).alt_boolean.value());
        assertEquals(CmsData.CHOICE_FLOAT64, decoded.alt_sequence.get(2).choice);
        assertEquals(3.14159, decoded.alt_sequence.get(2).alt_float64.value(), 1e-10);
    }
}
