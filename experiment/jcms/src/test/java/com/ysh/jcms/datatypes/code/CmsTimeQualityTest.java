package com.ysh.jcms.datatypes.code;

import com.ysh.jcms.datatypes.compound.CmsTimeQuality;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTimeQuality")
class CmsTimeQualityTest {

    @Test
    void construct() {
        CmsTimeQuality q = new CmsTimeQuality();
        assertEquals(0, q.tagf);
        assertEquals(0, q.precision);
        assertEquals(0L, q.fraction);
    }

    @Test
    void constructWithValues() {
        CmsTimeQuality q = new CmsTimeQuality(1, 3, 100L);
        assertEquals(1, q.tagf);
        assertEquals(3, q.precision);
        assertEquals(100L, q.fraction);
    }

    @Test
    void copy() {
        CmsTimeQuality original = new CmsTimeQuality(1, 3, 100L);
        CmsTimeQuality cloned = original.copy();
        assertEquals(original.tagf, cloned.tagf);
        assertEquals(original.precision, cloned.precision);
        assertEquals(original.fraction, cloned.fraction);
    }
}
