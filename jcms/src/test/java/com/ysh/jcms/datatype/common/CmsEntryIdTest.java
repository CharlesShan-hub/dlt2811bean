package com.ysh.jcms.datatype.common;

import com.ysh.jcms.datatype.extended.CmsBinaryTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsEntryId")
class CmsEntryIdTest {

    private CmsEntryId get() { return (CmsEntryId)(new CmsEntryId().test()); }

    private final byte[] SAMPLE = new byte[]{0, 0, 0, 0, 0, 0, 0, 1};

    @Test
    void roundtrip() {
        CmsEntryId original = get().value(SAMPLE);
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void defaultValueIsEightZeros() {
        CmsEntryId e = get();
        assertArrayEquals(new byte[8], e.value());
    }

    @Test
    void decodeOverwrites() {
        CmsEntryId src = get().value(SAMPLE);
        CmsEntryId target = get().decode(src.encode());
        assertArrayEquals(SAMPLE, target.value());
    }

    @Test
    void fromLong() {
        CmsEntryId e = get().value(1L);
        assertArrayEquals(SAMPLE, e.value());
    }

    @Test
    void fromLargeLong() {
        CmsEntryId e = get().value(0xDEADBEEFCAFEL);
        byte[] v = e.value();
        assertEquals(8, v.length);
        assertEquals(0x00, v[0] & 0xFF);
        assertEquals(0xDE, v[2] & 0xFF);
        assertEquals(0xBE, v[4] & 0xFF);
        assertEquals(0xEF, v[5] & 0xFF);
    }

    @Test
    void fromBinaryTime() {
        CmsBinaryTime bt = ((CmsBinaryTime) new CmsBinaryTime().test()).set(1718015445500L);
        CmsEntryId e = get().from(bt);
        byte[] v = e.value();
        assertEquals(8, v.length);
        assertEquals(0, v[6]);
        assertEquals(0, v[7]);
        byte[] btBytes = bt.encode();
        assertEquals(6, btBytes.length);
        for (int i = 0; i < 6; i++) {
            assertEquals(btBytes[i], v[i], "byte " + i);
        }
    }

    @Test
    void fixedSize() {
        assertEquals(8, CmsEntryId.LEN);
    }
}
