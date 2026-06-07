package com.ysh.jcms.datatype.extended;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTimeQuality")
class CmsTimeQualityTest {

    @Test
    void defaultPrecisionIsNotSpecified() {
        CmsTimeQuality q = new CmsTimeQuality();
        assertEquals(31, q.precision().value());  // 11111 = not specified
    }

    @Test
    void defaultFlagsAreFalse() {
        CmsTimeQuality q = new CmsTimeQuality();
        assertFalse(q.leap_seconds_known().value());
        assertFalse(q.clock_failure().value());
        assertFalse(q.clock_not_synchronized().value());
    }

    @Test
    void roundtripDefault() {
        assertEquals(new CmsTimeQuality(),
                     new CmsTimeQuality().decode(new CmsTimeQuality().encode()));
    }

    @Test
    void roundtripWithLeapSecondKnown() {
        CmsTimeQuality q = new CmsTimeQuality();
        q.leap_seconds_known().value(true);
        assertEquals(q, new CmsTimeQuality().decode(q.encode()));
    }

    @Test
    void roundtripWithClockFailure() {
        CmsTimeQuality q = new CmsTimeQuality();
        q.clock_failure().value(true);
        assertEquals(q, new CmsTimeQuality().decode(q.encode()));
    }

    @Test
    void roundtripWithClockNotSynced() {
        CmsTimeQuality q = new CmsTimeQuality();
        q.clock_not_synchronized().value(true);
        assertEquals(q, new CmsTimeQuality().decode(q.encode()));
    }

    @Test
    void multipleFlags() {
        CmsTimeQuality q = new CmsTimeQuality();
        q.leap_seconds_known().value(true);
        q.clock_failure().value(true);
        q.clock_not_synchronized().value(true);
        assertEquals(q, new CmsTimeQuality().decode(q.encode()));
    }

    @Test
    void precisionPreserved() {
        CmsTimeQuality q = new CmsTimeQuality();
        q.precision().value(10);  // 10 bits
        CmsTimeQuality decoded = new CmsTimeQuality().decode(q.encode());
        assertEquals(10, decoded.precision().value());
    }

    @Test
    void precisionMax() {
        CmsTimeQuality q = new CmsTimeQuality();
        q.precision().value(31);  // not specified
        assertEquals(q, new CmsTimeQuality().decode(q.encode()));
    }

    @Test
    void precisionZero() {
        CmsTimeQuality q = new CmsTimeQuality();
        q.precision().value(0);
        assertEquals(q, new CmsTimeQuality().decode(q.encode()));
    }
}
