// Auto-generated. Tests for CmsInt16

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsInt16Test {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsInt16 obj = new CmsInt16();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsInt16 obj = new CmsInt16(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsInt16 obj = new CmsInt16(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsInt16 d = MAPPER.readValue(json, CmsInt16.class);
        assertEquals(obj, d);
    }
}
