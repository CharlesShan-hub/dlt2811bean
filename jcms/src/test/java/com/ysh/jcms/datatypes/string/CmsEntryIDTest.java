package com.ysh.jcms.datatypes.string;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsEntryID")
class CmsEntryIDTest {

    @Test
    void roundtrip() {
        byte[] id = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        byte[] data = new CmsEntryID(id).encode();
        CmsEntryID decoded = CmsEntryID.decode(data);
        assertArrayEquals(id, decoded.get());
    }

    @Test
    void invalidLengthThrows() {
        assertThrows(IllegalArgumentException.class, () -> new CmsEntryID(new byte[]{0x01, 0x02}));
    }

    @Test
    void copy() {
        byte[] id = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        CmsEntryID original = new CmsEntryID(id);
        CmsEntryID cloned = original.copy();
        assertArrayEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }
}
