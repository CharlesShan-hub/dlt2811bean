// Auto-generated. Tests for CmsInt8

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsInt8Test {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsInt8 obj = new CmsInt8();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsInt8 obj = new CmsInt8(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsInt8 obj = new CmsInt8(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsInt8 d = MAPPER.readValue(json, CmsInt8.class);
        assertEquals(obj, d);
    }
}
