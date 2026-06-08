package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsDbpos")
class CmsDbposTest {

    private CmsDbpos get() { return (CmsDbpos)(new CmsDbpos().test()); }

    @Test
    void intermediate() {
        CmsDbpos a = get().value(CmsDbpos.INTERMEDIATE);
        CmsDbpos b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void off() {
        CmsDbpos a = get().value(CmsDbpos.OFF);
        CmsDbpos b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void on() {
        CmsDbpos a = get().value(CmsDbpos.ON);
        CmsDbpos b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void badState() {
        CmsDbpos a = get().value(CmsDbpos.BAD_STATE);
        CmsDbpos b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValue() {
        assertEquals(0, get().value());
    }

    @Test
    void decodeOverwrites() {
        CmsDbpos src = get().value(CmsDbpos.ON);
        CmsDbpos target = get().decode(src.encode());
        assertEquals(src, target);
    }
}
