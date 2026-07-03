package com.ysh.jcms.data.choice;

import com.ysh.jcms.data.common.*;

import java.math.BigInteger;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsDataTest {

    @Test
    public void roundup_error() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_ERROR);
        a.alt_error.value(CmsServiceError.ACCESS_VIOLATION);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_boolean() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_BOOLEAN);
        a.alt_boolean.value(true);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_int8() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT8);
        a.alt_int8.value(-42);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_int16() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT16);
        a.alt_int16.value(-30000);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_int32() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT32);
        a.alt_int32.value(-2000000000);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_int64() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT64);
        a.alt_int64.value(-9000000000000000000L);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_int8u() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT8U);
        a.alt_int8u.value(200);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_int16u() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT16U);
        a.alt_int16u.value(60000);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_int32u() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT32U);
        a.alt_int32u.value(3000000000L);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_int64u() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_INT64U);
        a.alt_int64u.value(new BigInteger("10000000000000000"));

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_float32() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_FLOAT32);
        a.alt_float32.value(3.14f);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_float64() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_FLOAT64);
        a.alt_float64.value(2.718281828459045);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_bit_string() {
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
    public void roundup_octet_string() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_OCTET_STRING);
        a.alt_octet_string.value("Hello".getBytes());

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_visible_string() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_VISIBLE_STRING);
        a.alt_visible_string.value("ABC-123".getBytes());

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_unicode_string() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_UNICODE_STRING);
        a.alt_unicode_string.value("你好世界".getBytes());

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_utc_time() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_UTC_TIME);
        a.alt_utc_time.secondsSinceEpoch.value(1234567890L);
        a.alt_utc_time.fractionOfSecond.value(500000);
        a.alt_utc_time.timeQuality.leap_seconds_known.value(true);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_binary_time() {
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
    public void roundup_quality() {
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
    public void roundup_dbpos() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_DBPOS);
        a.alt_dbpos.value(CmsDbpos.ON);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_tcmd() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_TCMD);
        a.alt_tcmd.value(CmsTcmd.OPERATE);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_check() {
        CmsData a = new CmsData();
        a.choice.value(CmsData.CHOICE_CHECK);
        a.alt_check.syncheck.value(true);
        a.alt_check.interlock_check.value(false);

        byte[] encoded = a.encode();
        CmsData b = new CmsData();
        b.decode(encoded);

        assertEquals(a, b);
    }

    @Test
    public void roundup_array_of_data() {
        /* CmsData with CHOICE_ARRAY → CmsArray<CmsData> */
        CmsData outer = new CmsData();
        outer.choice.value(CmsData.CHOICE_ARRAY);

        CmsData d1 = new CmsData();
        d1.choice.value(CmsData.CHOICE_INT32);
        d1.alt_int32.value(12345);

        CmsData d2 = new CmsData();
        d2.choice.value(CmsData.CHOICE_BOOLEAN);
        d2.alt_boolean.value(true);

        CmsData d3 = new CmsData();
        d3.choice.value(CmsData.CHOICE_FLOAT64);
        d3.alt_float64.value(3.14159);

        outer.alt_sequence.add(d1).add(d2).add(d3);
        System.out.println("before encode: " + outer.toString());

        byte[] encoded = outer.encode();
        System.out.println("after encode, encoded " + encoded.length + " bytes");

        CmsData decoded = new CmsData();
        decoded.decode(encoded);

        System.out.println("decoded.choice = " + decoded.choice.value() + " (expected " + CmsData.CHOICE_ARRAY + ")");
        System.out.println("decoded.alt_sequence.items.size = " + decoded.alt_sequence.items.size() + " (expected 3)");
        for (int i = 0; i < decoded.alt_sequence.items.size(); i++) {
            CmsData item = decoded.alt_sequence.items.get(i);
            System.out.println("  item[" + i + "].choice = " + item.choice.value());
        }
        System.out.println("item[0].alt_int32.value = " + decoded.alt_sequence.items.get(0).alt_int32.value() + " (expected 12345)");
        System.out.println("item[1].alt_boolean.value = " + decoded.alt_sequence.items.get(1).alt_boolean.value() + " (expected true)");
        System.out.println("item[2].alt_float64.value = " + decoded.alt_sequence.items.get(2).alt_float64.value() + " (expected 3.14159)");

        assertEquals(1, decoded.choice.value());
        assertEquals(3, decoded.alt_sequence.items.size());
        assertEquals(6, decoded.alt_sequence.items.get(0).choice.value());
        assertEquals(12345, decoded.alt_sequence.items.get(0).alt_int32.value());
        assertEquals(3, decoded.alt_sequence.items.get(1).choice.value());
        assertEquals(true, decoded.alt_sequence.items.get(1).alt_boolean.value());
        assertEquals(13, decoded.alt_sequence.items.get(2).choice.value());
        assertEquals(3.14159, decoded.alt_sequence.items.get(2).alt_float64.value(), 1e-10);
    }
}