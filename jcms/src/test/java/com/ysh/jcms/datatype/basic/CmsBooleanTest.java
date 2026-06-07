package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBoolean")
class CmsBooleanTest {

    @Test
    void trueRoundtrip() {
        assertEquals(new CmsBoolean().value(true),
                     new CmsBoolean().decode(new CmsBoolean().value(true).encode()));
    }

    @Test
    void falseRoundtrip() {
        assertEquals(new CmsBoolean().value(false),
                     new CmsBoolean().decode(new CmsBoolean().value(false).encode()));
    }

    @Test
    void defaultValue() {
        assertEquals(false, new CmsBoolean().value());
    }

    @Test
    void decodeOverwrites() {
        CmsBoolean target = new CmsBoolean().value(true);
        target.decode(new CmsBoolean().value(false).encode());
        assertEquals(new CmsBoolean().value(false), target);
    }
}
