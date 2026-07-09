package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class ValueMapperTest {

    private SclDataTypeTemplates parseTemplates() {
        try {
            com.ysh.jcms.utils.scl.reader.SclReader reader = new com.ysh.jcms.utils.scl.reader.SclReader();
            InputStream is = getClass().getClassLoader().getResourceAsStream("sample-scd-full.scd");
            return reader.read(is).dataTypeTemplates();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMapValueBoolean() {
        ValueMapper vm = new ValueMapper(null);
        assertEquals(true, vm.mapValue("BOOLEAN", "true").get());
        assertEquals(false, vm.mapValue("BOOLEAN", "false").get());
    }

    @Test
    public void testMapValueInt32() {
        ValueMapper vm = new ValueMapper(null);
        assertEquals(42, vm.mapValue("INT32", "42").get());
    }

    @Test
    public void testMapValueFloat32() {
        ValueMapper vm = new ValueMapper(null);
        assertEquals(3.14f, (Float) vm.mapValue("FLOAT32", "3.14").get(), 0.001f);
    }

    @Test
    public void testMapValueString() {
        ValueMapper vm = new ValueMapper(null);
        assertEquals("hello", vm.mapValue("VisString255", "hello").get());
    }

    @Test
    public void testMapValueNull() {
        ValueMapper vm = new ValueMapper(null);
        assertFalse(vm.mapValue("INT32", null).isPresent());
    }

    @Test
    public void testMapEnumValue() {
        SclDataTypeTemplates templates = parseTemplates();
        ValueMapper vm = new ValueMapper(templates);

        // Beh enum: ord=1 → "on"
        assertEquals("on", vm.mapEnumValue("Beh", 1).get());
        assertEquals("off", vm.mapEnumValue("Beh", 5).get());
    }

    @Test
    public void testMapEnumOrd() {
        SclDataTypeTemplates templates = parseTemplates();
        ValueMapper vm = new ValueMapper(templates);

        // Health enum: "Ok" → ord=1
        assertEquals(Integer.valueOf(1), vm.mapEnumOrd("Health", "Ok").get());
        assertEquals(Integer.valueOf(3), vm.mapEnumOrd("Health", "Alarm").get());
    }
}
