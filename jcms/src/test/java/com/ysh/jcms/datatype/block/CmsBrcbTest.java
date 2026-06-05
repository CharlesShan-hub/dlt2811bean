package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBrcb")
class CmsBrcbTest {

    @Test
    void roundtrip() {
        CmsBrcb original = new CmsBrcb();
        original.rptID().bytes("BRCB_001");
        original.rptEna().value(1);
        original.datSet().bytes("DataSet_01");
        original.confRev().value(1);
        original.bufTm().value(1000);
        original.gi().value(0);
        original.purgeBuf().value(1);

        byte[] data = original.encode();
        CmsBrcb decoded = new CmsBrcb().decode(data);

        assertEquals("BRCB_001", new String(decoded.rptID().bytes()).trim());
        assertEquals(1, decoded.rptEna().value());
        assertEquals(1, decoded.confRev().value());
        assertEquals(1000, decoded.bufTm().value());
        assertEquals(0, decoded.gi().value());
        assertEquals(1, decoded.purgeBuf().value());
    }
}
