package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsLcb")
class CmsLcbTest {

    @Test
    void roundtrip() {
        CmsLcb original = new CmsLcb();
        original.logEna().value(true);
        original.logRef().value("LogRef_01");
        original.intgPd().value(5000);

        byte[] data = original.encode();
        CmsLcb decoded = new CmsLcb().decode(data);

        assertEquals(true, decoded.logEna().value());
        assertEquals(5000, decoded.intgPd().value());
    }
}
