package com.ysh.jcms.datatypes2.block;

import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.data.basic.CmsInt32U;
import com.ysh.jcms.datatypes2.data.block.CmsLcb;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsLcb")
class CmsLcbTest {

    @Test
    void roundtrip() {
        CmsLcb original = new CmsLcb();
        original.logEna = new CmsBoolean(true);
        original.logRef.set("LogRef_01");
        original.intgPd = new CmsInt32U(5000);

        byte[] data = original.encode();
        CmsLcb decoded = CmsLcb.from(data);

        assertTrue(decoded.logEna.get());
        assertEquals(5000, decoded.intgPd.longValue());
    }
}
