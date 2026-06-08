package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTimeStamp")
class CmsTimeStampTest {

    private CmsTimeStamp get() { return (CmsTimeStamp)(new CmsTimeStamp().test()); }

    @Test
    void roundtrip() {
        CmsTimeStamp original = get().set(2024, 6, 6, 10, 30, 45, 500);
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void setFromEpochMillis() {
        CmsTimeStamp original = get().set(1718015445500L);
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void setFromZonedDateTime() {
        CmsTimeStamp original = get()
            .set(ZonedDateTime.parse("2024-06-06T10:30:45.500Z"));
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void nowRoundtrip() {
        CmsTimeStamp original = get().now();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void subTypeAccess() {
        CmsTimeStamp t = get().set(1718015445500L);
        assertEquals(1718015445, t.seconds_since_epoch().value());
        assertTrue(t.fraction_of_second().value() > 0);
        assertTrue(t.time_quality().leap_seconds_known().value());
    }
}
