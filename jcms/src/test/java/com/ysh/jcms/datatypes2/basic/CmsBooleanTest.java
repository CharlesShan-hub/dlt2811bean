package com.ysh.jcms.datatypes2.basic;

import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBoolean")
class CmsBooleanTest {

    @Test
    void trueRoundtrip() {
        byte[] data = new CmsBoolean(true).encode();
        CmsBoolean r = CmsBoolean.from(data);
        assertTrue(r.get());
    }

    @Test
    void falseRoundtrip() {
        byte[] data = new CmsBoolean(false).encode();
        CmsBoolean r = CmsBoolean.from(data);
        assertFalse(r.get());
    }

    @Test
    void defaultValue() {
        assertFalse(new CmsBoolean().get());
    }

    @Test
    void decodeOverwrites() {
        CmsBoolean v = new CmsBoolean(true);
        v.decode(new CmsBoolean(false).encode());
        assertFalse(v.get());
    }
}
