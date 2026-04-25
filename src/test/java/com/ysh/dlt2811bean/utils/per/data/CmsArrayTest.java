package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsArray")
class CmsArrayTest {

    @Test
    @DisplayName("encode and decode with multiple elements")
    void encodeDecode() throws Exception {
        CmsArray<CmsInt32> array = new CmsArray<>(CmsInt32.class).capacity(10);
        array.add(new CmsInt32(100)).add(new CmsInt32(200)).add(new CmsInt32(300));

        PerOutputStream pos = new PerOutputStream();
        array.encode(pos);

        CmsArray<CmsInt32> decoded = new CmsArray<>(CmsInt32.class).capacity(10)
            .decode(new PerInputStream(pos.toByteArray()));

        assertEquals(3, decoded.size());
        assertEquals(100, (int) decoded.get(0).get());
        assertEquals(200, (int) decoded.get(1).get());
        assertEquals(300, (int) decoded.get(2).get());
    }

    @Test
    @DisplayName("empty array")
    void empty() throws Exception {
        CmsArray<CmsInt32> array = new CmsArray<>(CmsInt32.class).capacity(10);

        PerOutputStream pos = new PerOutputStream();
        array.encode(pos);

        CmsArray<CmsInt32> decoded = new CmsArray<>(CmsInt32.class).capacity(10)
            .decode(new PerInputStream(pos.toByteArray()));

        assertTrue(decoded.isEmpty());
        assertEquals(0, decoded.size());
    }

    @Test
    @DisplayName("single element")
    void single() throws Exception {
        CmsArray<CmsInt32> array = new CmsArray<>(CmsInt32.class).capacity(10);
        array.add(new CmsInt32(42));

        PerOutputStream pos = new PerOutputStream();
        array.encode(pos);

        CmsArray<CmsInt32> decoded = new CmsArray<>(CmsInt32.class).capacity(10)
            .decode(new PerInputStream(pos.toByteArray()));

        assertEquals(1, decoded.size());
        assertEquals(42, (int) decoded.get(0).get());
    }

    @Test
    @DisplayName("add and iterate")
    void iteration() {
        CmsArray<CmsInt32> array = new CmsArray<>(CmsInt32.class).capacity(10);
        array.add(new CmsInt32(1)).add(new CmsInt32(2)).add(new CmsInt32(3));

        int sum = 0;
        for (CmsInt32 item : array) {
            sum += item.get();
        }
        assertEquals(6, sum);
    }

    @Test
    @DisplayName("toList returns copy")
    void toList() {
        CmsArray<CmsInt32> array = new CmsArray<>(CmsInt32.class).capacity(10);
        array.add(new CmsInt32(10)).add(new CmsInt32(20));

        var list = array.toList();
        assertEquals(2, list.size());
        assertEquals(10, (int) list.get(0).get());
        assertEquals(20, (int) list.get(1).get());
    }

    @Test
    @DisplayName("stream support")
    void stream() {
        CmsArray<CmsInt32> array = new CmsArray<>(CmsInt32.class).capacity(10);
        array.add(new CmsInt32(1)).add(new CmsInt32(2)).add(new CmsInt32(3)).add(new CmsInt32(4)).add(new CmsInt32(5));

        long sum = array.stream().mapToInt(CmsInt32::get).sum();
        assertEquals(15, sum);
    }

    @Test
    @DisplayName("encode without capacity throws exception")
    void encodeWithoutCapacity() {
        CmsArray<CmsInt32> array = new CmsArray<>(CmsInt32.class);
        array.add(new CmsInt32(1));

        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalStateException.class, () -> array.encode(pos));
    }

    @Test
    @DisplayName("decode without capacity throws exception")
    void decodeWithoutCapacity() {
        CmsArray<CmsInt32> array = new CmsArray<>(CmsInt32.class);
        PerInputStream pis = new PerInputStream(new byte[0]);
        assertThrows(IllegalStateException.class, () -> array.decode(pis));
    }

    @Test
    @DisplayName("add null throws exception")
    void addNull() {
        CmsArray<CmsInt32> array = new CmsArray<>(CmsInt32.class).capacity(10);
        assertThrows(NullPointerException.class, () -> array.add(null));
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsArray<CmsInt32> array = new CmsArray<>(CmsInt32.class).capacity(10);
        array.add(new CmsInt32(100)).add(new CmsInt32(200));
        assertEquals("SEQUENCE OF[INT32: 100, INT32: 200]", array.toString());
    }

    @Test
    @DisplayName("toString empty")
    void toStringEmpty() {
        CmsArray<CmsInt32> array = new CmsArray<>(CmsInt32.class).capacity(10);
        assertEquals("SEQUENCE OF[]", array.toString());
    }

    @Test
    @DisplayName("encode and decode with CmsInt8U elements")
    void withCmsInt8U() throws Exception {
        CmsArray<CmsInt8U> array = new CmsArray<>(CmsInt8U.class).capacity(5);
        array.add(new CmsInt8U(10)).add(new CmsInt8U(20)).add(new CmsInt8U(30));

        PerOutputStream pos = new PerOutputStream();
        array.encode(pos);

        CmsArray<CmsInt8U> decoded = new CmsArray<>(CmsInt8U.class).capacity(5)
            .decode(new PerInputStream(pos.toByteArray()));

        assertEquals(3, decoded.size());
        assertEquals(10, (int) decoded.get(0).get());
        assertEquals(20, (int) decoded.get(1).get());
        assertEquals(30, (int) decoded.get(2).get());
    }
}