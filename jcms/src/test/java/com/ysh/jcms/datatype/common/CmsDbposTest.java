package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsDbpos")
class CmsDbposTest {

    @Test
    void intermediate() {
        CmsDbpos original = new CmsDbpos().value(CmsDbpos.INTERMEDIATE);
        assertEquals(original, new CmsDbpos().decode(original.encode()));
    }

    @Test
    void off() {
        CmsDbpos original = new CmsDbpos().value(CmsDbpos.OFF);
        assertEquals(original, new CmsDbpos().decode(original.encode()));
    }

    @Test
    void on() {
        CmsDbpos original = new CmsDbpos().value(CmsDbpos.ON);
        assertEquals(original, new CmsDbpos().decode(original.encode()));
    }

    @Test
    void badState() {
        CmsDbpos original = new CmsDbpos().value(CmsDbpos.BAD_STATE);
        assertEquals(original, new CmsDbpos().decode(original.encode()));
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsDbpos().value());
    }

    @Test
    void decodeOverwrites() {
        CmsDbpos target = new CmsDbpos().value(CmsDbpos.ON);
        target.decode(new CmsDbpos().value(CmsDbpos.OFF).encode());
        assertEquals(new CmsDbpos().value(CmsDbpos.OFF), target);
    }
}
