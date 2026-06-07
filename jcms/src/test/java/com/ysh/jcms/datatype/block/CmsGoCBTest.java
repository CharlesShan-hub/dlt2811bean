package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGoCB")
class CmsGoCBTest {

    @Test
    void roundtrip() {
        CmsGoCB original = new CmsGoCB();
        original.goEna().value(true);
        original.goID().value("GOOSE_01");
        original.ndsCom().value(false);

        byte[] data = original.encode();
        CmsGoCB decoded = new CmsGoCB().decode(data);

        assertEquals(true, decoded.goEna().value());
        assertEquals(false, decoded.ndsCom().value());
    }
}
