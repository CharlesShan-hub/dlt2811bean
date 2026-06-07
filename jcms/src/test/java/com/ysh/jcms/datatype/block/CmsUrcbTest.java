package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsUrcb")
class CmsUrcbTest {

    @Test
    void roundtrip() {
        CmsUrcb original = new CmsUrcb();
        original.rptID().value("URCB_001");
        original.rptEna().value(true);
        original.datSet().value("DataSet_01");
        original.confRev().value(1);
        original.bufTm().value(500);
        original.gi().value(true);
        original.resv().value(false);

        byte[] data = original.encode();
        CmsUrcb decoded = new CmsUrcb().decode(data);

        assertEquals("URCB_001", new String(decoded.rptID().value()).trim());
        assertEquals(true, decoded.rptEna().value());
        assertEquals(1, decoded.confRev().value());
        assertEquals(500, decoded.bufTm().value());
        assertEquals(true, decoded.gi().value());
        assertEquals(false, decoded.resv().value());
    }

    @Test
    void withOwner() {
        CmsUrcb original = new CmsUrcb();
        original.rptID().value("URCB_002");
        original.rptEna().value(true);
        original.owner().value("IED1");
        original.owner_present().value(true);

        byte[] data = original.encode();
        CmsUrcb decoded = new CmsUrcb().decode(data);

        assertEquals("URCB_002", new String(decoded.rptID().value()).trim());
        assertEquals(true, decoded.owner_present().value());
        assertEquals("IED1", new String(decoded.owner().value()).trim());
    }
}
