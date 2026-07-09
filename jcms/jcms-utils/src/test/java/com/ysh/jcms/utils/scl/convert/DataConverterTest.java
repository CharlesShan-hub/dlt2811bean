package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.data.choice.CmsData;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataConverterTest {

    @Test
    public void testBooleanTrue() {
        DataValueEntry dv = new DataValueEntry("ref", "true", "BOOLEAN");
        CmsData data = DataConverter.toCmsData(dv);
        assertEquals(CmsData.CHOICE_BOOLEAN, data.choice.value());
        assertTrue(data.alt_boolean.value());
    }

    @Test
    public void testInt32() {
        DataValueEntry dv = new DataValueEntry("ref", "42", "INT32");
        CmsData data = DataConverter.toCmsData(dv);
        assertEquals(CmsData.CHOICE_INT32, data.choice.value());
        assertEquals(42, data.alt_int32.value());
    }

    @Test
    public void testFloat32() {
        DataValueEntry dv = new DataValueEntry("ref", "3.14", "FLOAT32");
        CmsData data = DataConverter.toCmsData(dv);
        assertEquals(CmsData.CHOICE_FLOAT32, data.choice.value());
        assertEquals(3.14f, data.alt_float32.value(), 0.001f);
    }

    @Test
    public void testEnumAsInt32() {
        DataValueEntry dv = new DataValueEntry("ref", "5", "Enum");
        CmsData data = DataConverter.toCmsData(dv);
        assertEquals(CmsData.CHOICE_INT32, data.choice.value());
        assertEquals(5, data.alt_int32.value());
    }

    @Test
    public void testFallbackVisibleString() {
        DataValueEntry dv = new DataValueEntry("ref", "hello", "UNKNOWN");
        CmsData data = DataConverter.toCmsData(dv);
        assertEquals(CmsData.CHOICE_VISIBLE_STRING, data.choice.value());
    }

    @Test
    public void testAutoDetectBoolean() {
        CmsData data = DataConverter.autoDetect("true");
        assertEquals(CmsData.CHOICE_BOOLEAN, data.choice.value());
        assertTrue(data.alt_boolean.value());
    }

    @Test
    public void testAutoDetectInt8() {
        CmsData data = DataConverter.autoDetect("42");
        assertEquals(CmsData.CHOICE_INT8, data.choice.value()); // 42 fits in byte
    }
}
