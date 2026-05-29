package com.ysh.jcms.datatypes.string;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFC")
class CmsFCTest {

    @Test
    void roundtrip() {
        byte[] fcData = {(byte) 0xAB, (byte) 0xCD};
        byte[] data = new CmsFC(fcData).encode();
        CmsFC decoded = CmsFC.decode(data);
        assertArrayEquals(fcData, decoded.get());
    }

    @Test
    void invalidLengthThrows() {
        assertThrows(IllegalArgumentException.class, () -> new CmsFC(new byte[]{0x01}));
    }

    @Test
    void copy() {
        byte[] fcData = {(byte) 0xAB, (byte) 0xCD};
        CmsFC original = new CmsFC(fcData);
        CmsFC cloned = original.copy();
        assertArrayEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }
}
