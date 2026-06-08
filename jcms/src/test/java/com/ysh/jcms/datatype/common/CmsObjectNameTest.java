package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsObjectName")
class CmsObjectNameTest {

    private CmsObjectName get() { return (CmsObjectName)(new CmsObjectName().test()); }

    @Test
    void roundtrip() {
        CmsObjectName original = get().value("GGIO1");
        CmsObjectName decoded = get().decode(original.encode());
        assertEquals("GGIO1", new String(decoded.value()).trim());
    }

    @Test
    void maxLength() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 64; i++) sb.append('A');
        String s = sb.toString();
        CmsObjectName original = get().value(s);
        CmsObjectName decoded = get().decode(original.encode());
        assertEquals(s, new String(decoded.value()).trim());
    }

    @Test
    void empty() {
        CmsObjectName v = get().value("");
        assertEquals("", new String(v.value()).trim());
    }
}
