package com.ysh.jcms.datatypes.packed;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsPackedList")
class CmsPackedListTest {

    @Test
    void roundtrip() {
        byte[] data = {(byte) 0xAB, (byte) 0xCD};
        CmsPackedList original = new CmsPackedList(data);
        byte[] encoded = original.encode();
        CmsPackedList decoded = CmsPackedList.decode(encoded);
        assertArrayEquals(original.get(), decoded.get());
    }

    @Test
    void empty() {
        byte[] data = new byte[0];
        byte[] encoded = new CmsPackedList(data).encode();
        CmsPackedList decoded = CmsPackedList.decode(encoded);
        assertArrayEquals(data, decoded.get());
    }

    @Test
    void copy() {
        byte[] data = {(byte) 0xAB, (byte) 0xCD};
        CmsPackedList original = new CmsPackedList(data);
        CmsPackedList cloned = original.copy();
        assertArrayEquals(original.get(), cloned.get());
    }
}
