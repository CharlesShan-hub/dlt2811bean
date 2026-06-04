package com.ysh.jcms.datatypes.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsArray")
class CmsArrayTest {

    @Test
    void empty() {
        CmsArray arr = new CmsArray();
        assertTrue(arr.isEmpty());
        assertEquals(0, arr.size());
    }

    @Test
    void addAndGet() {
        CmsArray arr = new CmsArray();
        arr.add(CmsData.createBoolean(true));
        arr.add(CmsData.createInt8((byte) 42));

        assertEquals(2, arr.size());
        assertFalse(arr.isEmpty());
        assertTrue(arr.get(0).boolVal());
        assertEquals(42, arr.get(1).intVal());
    }

    @Test
    void constructorWithList() {
        List<CmsData> items = Arrays.asList(
                CmsData.createInt16((short) 100),
                CmsData.createInt32(2000)
        );
        CmsArray arr = new CmsArray(items);
        assertEquals(2, arr.size());
        assertEquals(100, arr.get(0).intVal());
        assertEquals(2000, arr.get(1).intVal());
    }

    @Test
    void addAll() {
        CmsArray arr = new CmsArray();
        arr.add(CmsData.createBoolean(false));
        arr.addAll(Arrays.asList(
                CmsData.createFloat32(1.5f),
                CmsData.createFloat64(2.5)
        ));
        assertEquals(3, arr.size());
    }

    @Test
    void copy() {
        CmsArray original = new CmsArray();
        original.add(CmsData.createInt8((byte) 1));
        original.add(CmsData.createBoolean(true));

        CmsArray cloned = original.copy();
        assertEquals(original.size(), cloned.size());
        assertEquals(original.get(0).intVal(), cloned.get(0).intVal());
    }

    @Test
    void elements() {
        CmsArray arr = new CmsArray();
        arr.add(CmsData.createBoolean(true));
        List<CmsData> elems = arr.elements();
        assertEquals(1, elems.size());
        assertTrue(elems.get(0).boolVal());
    }
}
