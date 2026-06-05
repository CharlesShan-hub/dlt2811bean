package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGoCB")
class CmsGoCBTest {

    @Test
    void roundtrip() {
        CmsGoCB original = new CmsGoCB();
        original.goEna().value(1);
        original.goID().bytes("GOOSE_01");
        original.ndsCom().value(0);

        byte[] data = original.encode();
        CmsGoCB decoded = new CmsGoCB().decode(data);

        assertEquals(1, decoded.goEna().value());
        assertEquals(0, decoded.ndsCom().value());
    }
}
