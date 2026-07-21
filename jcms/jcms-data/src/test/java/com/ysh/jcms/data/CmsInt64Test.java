// Auto-generated. Tests for CmsInt64

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsInt64Test {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsInt64 obj = new CmsInt64();
        assertEquals(0L, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsInt64 obj = new CmsInt64(42L);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsInt64 obj = new CmsInt64(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsInt64 d = MAPPER.readValue(json, CmsInt64.class);
        assertEquals(obj, d);
    }
}
