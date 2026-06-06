package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBoolean")
class CmsBooleanTest {

    @Test
    void trueRoundtrip() {
        assertEquals(new CmsBoolean().value(1),
                     new CmsBoolean().decode(new CmsBoolean().value(1).encode()));
    }

    @Test
    void falseRoundtrip() {
        assertEquals(new CmsBoolean().value(0),
                     new CmsBoolean().decode(new CmsBoolean().value(0).encode()));
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsBoolean().value());
    }

    @Test
    void decodeOverwrites() {
        CmsBoolean target = new CmsBoolean().value(1);
        target.decode(new CmsBoolean().value(0).encode());
        assertEquals(new CmsBoolean().value(0), target);
    }
}
