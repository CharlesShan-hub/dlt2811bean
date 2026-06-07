package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsLcbOptFlds")
class CmsLcbOptFldsTest {

    @Test
    void roundtripDefault() {
        assertEquals(new CmsLcbOptFlds(),
                     new CmsLcbOptFlds().decode(new CmsLcbOptFlds().encode()));
    }

    @Test
    void valueFalse() {
        CmsLcbOptFlds o = new CmsLcbOptFlds();
        o.value().value(false);
        assertEquals(o, new CmsLcbOptFlds().decode(o.encode()));
    }

    @Test
    void valueTrue() {
        CmsLcbOptFlds o = new CmsLcbOptFlds();
        o.value().value(true);
        assertEquals(o, new CmsLcbOptFlds().decode(o.encode()));
    }
}
