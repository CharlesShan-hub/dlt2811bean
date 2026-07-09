package com.ysh.jcms.utils.scl2.convert;

import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.string.CmsUint8Array;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeMapperTest {

    @Test
    public void testBoolean() {
        assertTrue(((CmsBoolean) TypeMapper.createTypedValue("BOOLEAN", "true")).value());
        assertFalse(((CmsBoolean) TypeMapper.createTypedValue("BOOLEAN", "false")).value());
    }

    @Test
    public void testInt32() {
        assertEquals(42, ((CmsInt32) TypeMapper.createTypedValue("INT32", "42")).value());
    }

    @Test
    public void testFloat32() {
        assertEquals(3.14f, ((CmsFloat32) TypeMapper.createTypedValue("FLOAT32", "3.14")).value(), 0.001f);
    }

    @Test
    public void testEnum() {
        assertEquals(2, ((CmsInt32) TypeMapper.createTypedValue("Enum", "2")).value());
    }

    @Test
    public void testFallback() {
        assertTrue(TypeMapper.createTypedValue("UNKNOWN", "hello") instanceof CmsUint8Array);
    }

    @Test
    public void testNull() {
        assertTrue(TypeMapper.createTypedValue(null, "hello") instanceof CmsUint8Array);
        assertTrue(TypeMapper.createTypedValue("INT32", null) instanceof CmsUint8Array);
    }
}
