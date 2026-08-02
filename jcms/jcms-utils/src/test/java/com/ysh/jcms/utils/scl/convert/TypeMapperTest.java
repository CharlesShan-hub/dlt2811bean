package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.data.choice.CmsData;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeMapperTest {

    @Test
    public void testBoolean() {
        assertTrue(TypeMapper.createTypedValue("BOOLEAN", "true").alt_boolean.value());
        assertFalse(TypeMapper.createTypedValue("BOOLEAN", "false").alt_boolean.value());
    }

    @Test
    public void testInt32() {
        assertEquals(42, TypeMapper.createTypedValue("INT32", "42").alt_int32.value());
    }

    @Test
    public void testFloat32() {
        assertEquals(3.14f, TypeMapper.createTypedValue("FLOAT32", "3.14").alt_float32.value(), 0.001f);
    }

    @Test
    public void testEnum() {
        assertEquals(2, TypeMapper.createTypedValue("Enum", "2").alt_int32.value());
    }

    @Test
    public void testFallback() {
        assertEquals(CmsData.CHOICE_OCTET_STRING, TypeMapper.createTypedValue("UNKNOWN", "hello").choice());
    }

    @Test
    public void testNull() {
        assertEquals(CmsData.CHOICE_VISIBLE_STRING, TypeMapper.createTypedValue(null, "hello").choice());
        assertEquals(CmsData.CHOICE_VISIBLE_STRING, TypeMapper.createTypedValue("INT32", null).choice());
    }
}
