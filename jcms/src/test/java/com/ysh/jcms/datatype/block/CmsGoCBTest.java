package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGoCB")
class CmsGoCBTest {

    private CmsGoCB get() { return (CmsGoCB)(new CmsGoCB().test()); }

    @Test
    void roundtrip() {
        CmsGoCB original = get();
        original.goEna().value(true);
        original.goID().value("GOOSE_01");
        original.ndsCom().value(false);

        CmsGoCB decoded = get().decode(original.encode());
        assertEquals(true, decoded.goEna().value());
        assertEquals(false, decoded.ndsCom().value());
    }
}
