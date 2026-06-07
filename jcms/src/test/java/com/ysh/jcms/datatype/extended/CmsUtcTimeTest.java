package com.ysh.jcms.datatype.extended;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsUtcTime")
class CmsUtcTimeTest {

    @Test
    void setFromComponents() {
        // 2024-06-06T10:30:45.500Z
        CmsUtcTime original = new CmsUtcTime().set(2024, 6, 6, 10, 30, 45, 500);
        assertEquals(original, new CmsUtcTime().decode(original.encode()));
    }

    @Test
    void setFromComponentsWithoutMillis() {
        CmsUtcTime original = new CmsUtcTime().set(2024, 6, 6, 10, 30, 45);
        assertEquals(original, new CmsUtcTime().decode(original.encode()));
    }

    @Test
    void setFromEpochMillis() {
        // 2024-06-06T10:30:45.500Z
        CmsUtcTime original = new CmsUtcTime().set(1718015445500L);
        assertEquals(original, new CmsUtcTime().decode(original.encode()));
    }

    @Test
    void setFromZonedDateTime() {
        CmsUtcTime original = new CmsUtcTime()
            .set(ZonedDateTime.parse("2024-06-06T10:30:45.500Z"));
        assertEquals(original, new CmsUtcTime().decode(original.encode()));
    }

    @Test
    void epochZero() {
        // 1970-01-01T00:00:00.000Z
        CmsUtcTime t = new CmsUtcTime().set(1970, 1, 1, 0, 0, 0);
        assertEquals(0, t.seconds_since_epoch().value());
        assertEquals(0, t.fraction_of_second().value());
    }

    @Test
    void nowRoundtrip() {
        CmsUtcTime original = new CmsUtcTime().now();
        assertEquals(original, new CmsUtcTime().decode(original.encode()));
    }

    @Test
    void setNanos() {
        // 1718015445500123456 ns = 2024-06-06T10:30:45.500123456Z
        CmsUtcTime original = new CmsUtcTime().setNanos(1718015445500123456L);
        assertEquals(1718015445, original.seconds_since_epoch().value());
        assertTrue(original.fraction_of_second().value() > 0);
        assertEquals(original, new CmsUtcTime().decode(original.encode()));
    }
}
