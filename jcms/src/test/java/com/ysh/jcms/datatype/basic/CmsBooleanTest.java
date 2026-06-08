package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBoolean")
class CmsBooleanTest {

    private CmsBoolean getCmsBoolean() {
        return (CmsBoolean)(new CmsBoolean().test());
    }

    @Test
    void trueRoundtrip() {
        CmsBoolean a = getCmsBoolean().value(true);
        CmsBoolean b = getCmsBoolean().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void falseRoundtrip() {
        CmsBoolean a = getCmsBoolean().value(false);
        CmsBoolean b = getCmsBoolean().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValue() {
        assertEquals(false, getCmsBoolean().value());
    }

    @Test
    void decodeOverwrites() {
        CmsBoolean a = getCmsBoolean().value(true);
        CmsBoolean b = getCmsBoolean().decode(a.encode());
        assertEquals(a, b);
    }
}
