package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsLcb")
class CmsLcbTest {

    private CmsLcb get() { return (CmsLcb)(new CmsLcb().test()); }

    @Test
    void roundtrip() {
        CmsLcb original = get();
        original.logEna().value(true);
        original.logRef().value("LogRef_01");
        original.intgPd().value(5000);

        CmsLcb decoded = get().decode(original.encode());
        assertEquals(true, decoded.logEna().value());
        assertEquals(5000, decoded.intgPd().value());
    }
}
