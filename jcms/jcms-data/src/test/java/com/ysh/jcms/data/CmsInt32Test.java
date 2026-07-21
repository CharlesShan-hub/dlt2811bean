// Auto-generated. Tests for CmsInt32

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsInt32Test {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsInt32 obj = new CmsInt32();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsInt32 obj = new CmsInt32(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsInt32 obj = new CmsInt32(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsInt32 d = MAPPER.readValue(json, CmsInt32.class);
        assertEquals(obj, d);
    }
}
