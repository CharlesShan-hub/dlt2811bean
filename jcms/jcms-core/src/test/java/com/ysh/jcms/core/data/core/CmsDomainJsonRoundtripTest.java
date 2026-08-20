package com.ysh.jcms.core.data.core;

import com.ysh.jcms.core.data.bitarray.CmsQuality;
import com.ysh.jcms.core.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.scalar.CmsInt32;
import com.ysh.jcms.core.data.sequence.common.*;
import com.ysh.jcms.data.V;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests that domain JSON roundtrips correctly for all Cms* types.
 *
 * <p>
 * For each type: build a value → toJson() → fromJson() → verify equals.
 */
public class CmsDomainJsonRoundtripTest {

    /* ═══════════════════ CmsScalar ═══════════════════ */

    @Test
    public void scalarInt32() {
        CmsInt32 a = new CmsInt32().value(42);
        String json = a.toJson();
        CmsInt32 b = CmsType.fromJson(CmsInt32.class, json);
        assertEquals(a.value(), b.value());
    }

    /* ═══════════════════ CmsEnum ═══════════════════ */

    @Test
    public void enumeration() {
        CmsServiceError a = new CmsServiceError().value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        String json = a.toJson();
        CmsServiceError b = CmsType.fromJson(CmsServiceError.class, json);
        assertEquals(a.value(), b.value());
    }

    /* ═══════════════════ CmsNull ═══════════════════ */

    @Test
    public void nullType() {
        CmsNull a = new CmsNull();
        String json = a.toJson();
        assertEquals("null", json);
        CmsNull b = CmsType.fromJson(CmsNull.class, json);
        assertNotNull(b);
    }

    /* ═══════════════════ CmsBits ═══════════════════ */

    @Test
    public void bitField() {
        CmsTimeQuality a = new CmsTimeQuality()
                .leap_seconds_known(true)
                .clock_failure(false)
                .clock_not_synchronized(true)
                .precision(24);
        String json = a.toJson();
        CmsTimeQuality b = CmsType.fromJson(CmsTimeQuality.class, json);
        assertEquals(a.leap_seconds_known, b.leap_seconds_known);
        assertEquals(a.clock_failure, b.clock_failure);
        assertEquals(a.clock_not_synchronized, b.clock_not_synchronized);
        assertEquals(a.precision, b.precision);
    }

    /* ═══════════════════ CmsUtcTime ═══════════════════ */

    @Test
    public void utcTime() {
        CmsUtcTime a = new CmsUtcTime()
                .secondsSinceEpoch(1234567890L)
                .fractionOfSecond(500000)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(true).precision(24));
        String json = a.toJson();
        assertTrue(json.contains("secondsSinceEpoch"));
        assertTrue(json.contains("1234567890"));
        CmsUtcTime b = CmsType.fromJson(CmsUtcTime.class, json);
        // Compare field by field (CmsUtcTime has no equals())
        assertEquals(a.secondsSinceEpoch.value(), b.secondsSinceEpoch.value());
        assertEquals(a.fractionOfSecond.value(), b.fractionOfSecond.value());
        assertEquals(a.timeQuality.leap_seconds_known, b.timeQuality.leap_seconds_known);
        assertEquals(a.timeQuality.precision, b.timeQuality.precision);
    }

    /* ═══════════════════ CmsBinaryTime ═══════════════════ */

    @Test
    public void binaryTime() {
        CmsBinaryTime a = new CmsBinaryTime().msOfDay(12345678L).daysSince1984(10000);
        String json = a.toJson();
        assertTrue(json.contains("msOfDay"));
        assertTrue(json.contains("daysSince1984"));
        CmsBinaryTime b = CmsType.fromJson(CmsBinaryTime.class, json);
        assertEquals(a.msOfDay.value(), b.msOfDay.value());
        assertEquals(a.daysSince1984.value(), b.daysSince1984.value());
    }

    /* ═══════════════════ CmsSequence ═══════════════════ */

    @Test
    public void sequence() {
        CmsFileEntry a = new CmsFileEntry()
                .fileName("test.txt")
                .fileSize(1024L)
                .lastModified(new CmsUtcTime().secondsSinceEpoch(1234567890L))
                .checkSum(0xDEADBEEFL);
        String json = a.toJson();
        assertTrue(json.contains("fileName"));
        assertTrue(json.contains("test.txt"));
        assertTrue(json.contains("fileSize"));
        CmsFileEntry b = CmsType.fromJson(CmsFileEntry.class, json);
        assertEquals(a.fileName.value(), b.fileName.value());
        assertEquals(a.fileSize.value(), b.fileSize.value());
        assertEquals(a.checkSum.value(), b.checkSum.value());
        assertEquals(a.lastModified.secondsSinceEpoch.value(), b.lastModified.secondsSinceEpoch.value());
    }

    /* ═══════════════════ CmsData (Choice) ═══════════════════ */

    @Test
    public void dataInt32() {
        CmsData a = new CmsData().alt_int32(42);
        String json = a.toJson();
        assertTrue(json.contains("int32"));
        assertTrue(json.contains("42"));
        CmsData b = CmsType.fromJson(CmsData.class, json);
        assertEquals(a.choice(), b.choice());
        assertEquals(a.alt_int32.value(), b.alt_int32.value());
    }

    @Test
    public void dataBoolean() {
        CmsData a = new CmsData().alt_boolean(true);
        String json = a.toJson();
        CmsData b = CmsType.fromJson(CmsData.class, json);
        assertEquals(a.choice(), b.choice());
        assertEquals(a.alt_boolean.value(), b.alt_boolean.value());
    }

    @Test
    public void dataVisibleString() {
        CmsData a = new CmsData().alt_visible_string("hello");
        String json = a.toJson();
        CmsData b = CmsType.fromJson(CmsData.class, json);
        assertEquals(a.choice(), b.choice());
        assertEquals(V.getVal(a.alt_visible_string._v), V.getVal(b.alt_visible_string._v));
    }

    @Test
    public void dataQuality() {
        CmsData a = new CmsData().alt_quality(new CmsQuality()
                .validity(0).overflow(false).failure(true));
        String json = a.toJson();
        assertTrue(json.contains("quality"));
        CmsData b = CmsType.fromJson(CmsData.class, json);
        assertEquals(a.choice(), b.choice());
        assertEquals(a.alt_quality.validity, b.alt_quality.validity);
        assertEquals(a.alt_quality.failure, b.alt_quality.failure);
    }

    @Test
    public void dataUtcTime() {
        CmsUtcTime utc = new CmsUtcTime()
                .secondsSinceEpoch(1234567890L)
                .fractionOfSecond(500000)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(true));
        CmsData a = new CmsData().alt_utc_time(utc);
        String json = a.toJson();
        assertTrue(json.contains("utc-time"));
        CmsData b = CmsType.fromJson(CmsData.class, json);
        assertEquals(a.choice(), b.choice());
        assertEquals(a.alt_utc_time.secondsSinceEpoch.value(), b.alt_utc_time.secondsSinceEpoch.value());
        assertEquals(a.alt_utc_time.timeQuality.leap_seconds_known, b.alt_utc_time.timeQuality.leap_seconds_known);
    }

    @Test
    public void dataBinaryTime() {
        CmsData a = new CmsData().alt_binary_time(new CmsBinaryTime().msOfDay(12345678L).daysSince1984(10000));
        String json = a.toJson();
        assertTrue(json.contains("binary-time"));
        CmsData b = CmsType.fromJson(CmsData.class, json);
        assertEquals(a.choice(), b.choice());
        assertEquals(a.alt_binary_time.msOfDay.value(), b.alt_binary_time.msOfDay.value());
        assertEquals(a.alt_binary_time.daysSince1984.value(), b.alt_binary_time.daysSince1984.value());
    }

    @Test
    public void dataServiceError() {
        CmsData a = new CmsData().alt_error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        String json = a.toJson();
        assertTrue(json.contains("error"));
        assertTrue(json.contains("1"));
        CmsData b = CmsType.fromJson(CmsData.class, json);
        assertEquals(a.choice(), b.choice());
        assertEquals(a.alt_error.value(), b.alt_error.value());
    }

    @Test
    public void dataArray() {
        CmsData a = new CmsData().choice(CmsData.CHOICE_ARRAY);
        a.alt_sequence.add(new CmsData().alt_int32(1));
        a.alt_sequence.add(new CmsData().alt_int32(2));
        a.alt_sequence.add(new CmsData().alt_int32(3));
        String json = a.toJson();
        assertTrue(json.contains("array"));
        CmsData b = CmsType.fromJson(CmsData.class, json);
        assertEquals(a.choice(), b.choice());
        assertEquals(a.alt_sequence.size(), b.alt_sequence.size());
        assertEquals(a.alt_sequence.get(0).alt_int32.value(), b.alt_sequence.get(0).alt_int32.value());
        assertEquals(a.alt_sequence.get(1).alt_int32.value(), b.alt_sequence.get(1).alt_int32.value());
        assertEquals(a.alt_sequence.get(2).alt_int32.value(), b.alt_sequence.get(2).alt_int32.value());
    }

    /* ═══════════════════ CmsData 静态 fromJson 兼容 ═══════════════════ */

    @Test
    public void cmsDataStaticFromJson() {
        // Old-style auto-detection still works
        CmsData d = CmsData.fromJson("42");
        assertEquals(CmsData.CHOICE_VISIBLE_STRING, d.choice());

        d = CmsData.fromJson("{\"quality\":{\"validity\":0, \"overflow\":true}}");
        assertEquals(CmsData.CHOICE_QUALITY, d.choice());
        assertEquals(0, d.alt_quality.validity);
        assertTrue(d.alt_quality.overflow);
    }
}