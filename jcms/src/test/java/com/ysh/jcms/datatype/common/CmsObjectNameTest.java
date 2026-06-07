package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsObjectName")
class CmsObjectNameTest {

    @Test
    void roundtrip() {
        CmsObjectName original = new CmsObjectName().value("GGIO1");
        byte[] data = original.encode();
        CmsObjectName decoded = new CmsObjectName().decode(data);
        assertEquals("GGIO1", new String(decoded.value()).trim());
    }

    @Test
    void maxLength() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 64; i++) sb.append('A');
        String s = sb.toString();
        CmsObjectName original = new CmsObjectName().value(s);
        CmsObjectName decoded = new CmsObjectName().decode(original.encode());
        assertEquals(s, new String(decoded.value()).trim());
    }

    @Test
    void empty() {
        CmsObjectName v = new CmsObjectName().value("");
        assertEquals("", new String(v.value()).trim());
    }
}
