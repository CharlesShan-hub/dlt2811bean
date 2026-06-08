package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBrcb")
class CmsBrcbTest {

    private CmsBrcb get() { return (CmsBrcb)(new CmsBrcb().test()); }

    @Test
    void roundtrip() {
        CmsBrcb original = get();
        original.rptID().value("BRCB_001");
        original.rptEna().value(true);
        original.datSet().value("DataSet_01");
        original.confRev().value(1);
        original.bufTm().value(1000);
        original.gi().value(false);
        original.purgeBuf().value(true);

        CmsBrcb decoded = get().decode(original.encode());
        assertEquals("BRCB_001", new String(decoded.rptID().value()).trim());
        assertEquals(true, decoded.rptEna().value());
        assertEquals(1, decoded.confRev().value());
        assertEquals(1000, decoded.bufTm().value());
        assertEquals(false, decoded.gi().value());
        assertEquals(true, decoded.purgeBuf().value());
    }
}
