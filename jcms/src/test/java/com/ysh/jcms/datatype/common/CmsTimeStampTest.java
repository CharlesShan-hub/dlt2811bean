package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTimeStamp")
class CmsTimeStampTest {

    @Test
    void roundtrip() {
        CmsTimeStamp original = new CmsTimeStamp().set(2024, 6, 6, 10, 30, 45, 500);
        assertEquals(original, new CmsTimeStamp().decode(original.encode()));
    }

    @Test
    void setFromEpochMillis() {
        CmsTimeStamp original = new CmsTimeStamp().set(1718015445500L);
        assertEquals(original, new CmsTimeStamp().decode(original.encode()));
    }

    @Test
    void setFromZonedDateTime() {
        CmsTimeStamp original = new CmsTimeStamp()
            .set(ZonedDateTime.parse("2024-06-06T10:30:45.500Z"));
        assertEquals(original, new CmsTimeStamp().decode(original.encode()));
    }

    @Test
    void nowRoundtrip() {
        CmsTimeStamp original = new CmsTimeStamp().now();
        assertEquals(original, new CmsTimeStamp().decode(original.encode()));
    }

    @Test
    void subTypeAccess() {
        CmsTimeStamp t = new CmsTimeStamp().set(1718015445500L);
        assertEquals(1718015445, t.seconds_since_epoch().value());
        assertTrue(t.fraction_of_second().value() > 0);
        assertTrue(t.time_quality().leap_seconds_known().value());
    }
}
