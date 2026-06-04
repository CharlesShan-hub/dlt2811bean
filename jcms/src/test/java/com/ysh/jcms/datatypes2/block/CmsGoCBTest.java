package com.ysh.jcms.datatypes2.block;

import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.data.block.CmsGoCB;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGoCB")
class CmsGoCBTest {

    @Test
    void roundtrip() {
        CmsGoCB original = new CmsGoCB();
        original.goEna = new CmsBoolean(true);
        original.goID.set("GOOSE_01");
        original.ndsCom = new CmsBoolean(false);

        byte[] data = original.encode();
        CmsGoCB decoded = CmsGoCB.from(data);

        assertTrue(decoded.goEna.get());
        assertFalse(decoded.ndsCom.get());
    }
}
