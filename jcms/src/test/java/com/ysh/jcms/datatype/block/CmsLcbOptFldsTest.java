package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsLcbOptFlds")
class CmsLcbOptFldsTest {

    private CmsLcbOptFlds get() { return (CmsLcbOptFlds)(new CmsLcbOptFlds().test()); }

    @Test
    void roundtripDefault() {
        assertEquals(get(), get().decode(get().encode()));
    }

    @Test
    void valueFalse() {
        CmsLcbOptFlds o = get();
        o.value().value(false);
        assertEquals(o, get().decode(o.encode()));
    }

    @Test
    void valueTrue() {
        CmsLcbOptFlds o = get();
        o.value().value(true);
        assertEquals(o, get().decode(o.encode()));
    }
}
