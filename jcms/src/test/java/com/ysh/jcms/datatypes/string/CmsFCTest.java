package com.ysh.jcms.datatypes.string;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFC")
class CmsFCTest {

    @Test
    void roundtrip() {
        byte[] data = new CmsFC("ST").encode();
        CmsFC decoded = CmsFC.from(data);
        assertEquals("ST", decoded.get());
    }

    @Test
    void invalidLengthThrows() {
        assertThrows(IllegalArgumentException.class, () -> new CmsFC("X"));
    }

    @Test
    void copy() {
        CmsFC original = new CmsFC("MX");
        CmsFC cloned = original.copy();
        assertEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }
}
