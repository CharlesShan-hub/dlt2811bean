package com.ysh.jcms.datatypes.compound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsPhyComAddr")
class CmsPhyComAddrTest {

    @Test
    void roundtrip() {
        byte[] addr = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
        CmsPhyComAddr original = new CmsPhyComAddr(addr);
        byte[] data = original.encode();
        CmsPhyComAddr decoded = CmsPhyComAddr.from(data);
        assertArrayEquals(original.value(), decoded.value());
    }

    @Test
    void invalidLengthThrows() {
        assertThrows(IllegalArgumentException.class, () -> new CmsPhyComAddr(new byte[]{0x01}));
    }

    @Test
    void copy() {
        byte[] addr = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
        CmsPhyComAddr original = new CmsPhyComAddr(addr);
        CmsPhyComAddr cloned = original.copy();
        assertArrayEquals(original.value(), cloned.value());
        assertNotSame(original, cloned);
    }
}
