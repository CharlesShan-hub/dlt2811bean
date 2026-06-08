package com.ysh.jcms.datatype.extended;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTimeQuality")
class CmsTimeQualityTest {

    private CmsTimeQuality get() { return (CmsTimeQuality)(new CmsTimeQuality().test()); }

    @Test
    void defaultPrecisionIsNotSpecified() {
        CmsTimeQuality q = get();
        assertEquals(31, q.precision().value());  // 11111 = not specified
    }

    @Test
    void defaultFlagsAreFalse() {
        CmsTimeQuality q = get();
        assertFalse(q.leap_seconds_known().value());
        assertFalse(q.clock_failure().value());
        assertFalse(q.clock_not_synchronized().value());
    }

    @Test
    void roundtripDefault() {
        assertEquals(get(), get().decode(get().encode()));
    }

    @Test
    void roundtripWithLeapSecondKnown() {
        CmsTimeQuality q = get();
        q.leap_seconds_known().value(true);
        assertEquals(q, get().decode(q.encode()));
    }

    @Test
    void roundtripWithClockFailure() {
        CmsTimeQuality q = get();
        q.clock_failure().value(true);
        assertEquals(q, get().decode(q.encode()));
    }

    @Test
    void roundtripWithClockNotSynced() {
        CmsTimeQuality q = get();
        q.clock_not_synchronized().value(true);
        assertEquals(q, get().decode(q.encode()));
    }

    @Test
    void multipleFlags() {
        CmsTimeQuality q = get();
        q.leap_seconds_known().value(true);
        q.clock_failure().value(true);
        q.clock_not_synchronized().value(true);
        assertEquals(q, get().decode(q.encode()));
    }

    @Test
    void precisionPreserved() {
        CmsTimeQuality q = get();
        q.precision().value(10);  // 10 bits
        CmsTimeQuality decoded = get().decode(q.encode());
        assertEquals(10, decoded.precision().value());
    }

    @Test
    void precisionMax() {
        CmsTimeQuality q = get();
        q.precision().value(31);  // not specified
        assertEquals(q, get().decode(q.encode()));
    }

    @Test
    void precisionZero() {
        CmsTimeQuality q = get();
        q.precision().value(0);
        assertEquals(q, get().decode(q.encode()));
    }
}
