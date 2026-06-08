package com.ysh.jcms.datatype.extended;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsUtcTime")
class CmsUtcTimeTest {

    private CmsUtcTime get() { return (CmsUtcTime)(new CmsUtcTime().test()); }

    @Test
    void setFromComponents() {
        CmsUtcTime original = get().set(2024, 6, 6, 10, 30, 45, 500);
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void setFromComponentsWithoutMillis() {
        CmsUtcTime original = get().set(2024, 6, 6, 10, 30, 45);
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void setFromEpochMillis() {
        CmsUtcTime original = get().set(1718015445500L);
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void setFromZonedDateTime() {
        CmsUtcTime original = get()
            .set(ZonedDateTime.parse("2024-06-06T10:30:45.500Z"));
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void epochZero() {
        CmsUtcTime t = get().set(1970, 1, 1, 0, 0, 0);
        assertEquals(0, t.seconds_since_epoch().value());
        assertEquals(0, t.fraction_of_second().value());
    }

    @Test
    void nowRoundtrip() {
        CmsUtcTime original = get().now();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void setNanos() {
        CmsUtcTime original = get().setNanos(1718015445500123456L);
        assertEquals(1718015445, original.seconds_since_epoch().value());
        assertTrue(original.fraction_of_second().value() > 0);
        assertEquals(original, get().decode(original.encode()));
    }
}
