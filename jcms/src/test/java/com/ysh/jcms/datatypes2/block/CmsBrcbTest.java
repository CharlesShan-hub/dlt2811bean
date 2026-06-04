package com.ysh.jcms.datatypes2.block;

import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.data.basic.CmsInt32U;
import com.ysh.jcms.datatypes2.data.block.CmsBrcb;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBrcb")
class CmsBrcbTest {

    @Test
    void roundtrip() {
        CmsBrcb original = new CmsBrcb();
        original.rptID.set("BRCB_001");
        original.rptEna = new CmsBoolean(true);
        original.datSet.set("DataSet_01");
        original.confRev = new CmsInt32U(1);
        original.bufTm = new CmsInt32U(1000);
        original.gi = new CmsBoolean(false);
        original.purgeBuf = new CmsBoolean(true);

        byte[] data = original.encode();
        CmsBrcb decoded = CmsBrcb.from(data);

        assertEquals("BRCB_001", decoded.rptID.get());
        assertTrue(decoded.rptEna.get());
        assertEquals(1, decoded.confRev.longValue());
        assertEquals(1000, decoded.bufTm.longValue());
        assertFalse(decoded.gi.get());
        assertTrue(decoded.purgeBuf.get());
    }
}
