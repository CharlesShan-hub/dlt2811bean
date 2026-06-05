package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("CmsBoolean")
class CmsBooleanTest {

    @Test
    void trueRoundtrip() {
        byte[] data = new CmsBoolean().value(1).encode();
        CmsBoolean r = new CmsBoolean().decode(data);
        assertEquals(1, r.value());
    }

    @Test
    void falseRoundtrip() {
        byte[] data = new CmsBoolean().value(0).encode();
        CmsBoolean r = new CmsBoolean().decode(data);
        assertEquals(0, r.value());
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsBoolean().value());
    }

    @Test
    void decodeOverwrites() {
        CmsBoolean v = new CmsBoolean().value(1);
        v.decode(new CmsBoolean().value(0).encode());
        assertEquals(0, v.value());
    }
}
