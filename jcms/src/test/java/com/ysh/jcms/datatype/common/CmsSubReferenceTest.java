package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSubReference")
class CmsSubReferenceTest {

    @Test
    void roundtrip() {
        CmsSubReference original = CmsSubReference.of("LN", "DO", "DA", "BDA");
        assertEquals(original, new CmsSubReference().decode(original.encode()));
    }

    @Test
    void ofJoinsWithDot() {
        CmsSubReference ref = CmsSubReference.of("LN", "DO", "DA");
        assertEquals("LN.DO.DA", new String(ref.value()).trim());
    }

    @Test
    void singleSegment() {
        CmsSubReference ref = CmsSubReference.of("GGIO1");
        assertEquals("GGIO1", new String(ref.value()).trim());
        assertEquals(1, ref.segmentCount());
        assertEquals("GGIO1", ref.segment(0));
    }

    @Test
    void segmentCount() {
        assertEquals(4, CmsSubReference.of("LN", "DO", "DA", "BDA").segmentCount());
        assertEquals(2, CmsSubReference.of("DA", "BDA").segmentCount());
        assertEquals(0, new CmsSubReference().segmentCount());
    }

    @Test
    void segment() {
        CmsSubReference ref = CmsSubReference.of("LN", "DO", "DA", "BDA");
        assertEquals("LN", ref.segment(0));
        assertEquals("DO", ref.segment(1));
        assertEquals("DA", ref.segment(2));
        assertEquals("BDA", ref.segment(3));
        assertEquals("", ref.segment(4));  // out of range
        assertEquals("", ref.segment(-1)); // out of range
    }

    @Test
    void segments() {
        CmsSubReference ref = CmsSubReference.of("LN", "DO", "DA");
        assertArrayEquals(new String[]{"LN", "DO", "DA"}, ref.segments());
    }

    @Test
    void empty() {
        CmsSubReference ref = new CmsSubReference();
        assertEquals(0, ref.segmentCount());
        assertArrayEquals(new String[0], ref.segments());
    }
}
