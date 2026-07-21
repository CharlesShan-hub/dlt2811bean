// Auto-generated. Tests for CmsInt24U

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsInt24UTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsInt24U obj = new CmsInt24U();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsInt24U obj = new CmsInt24U(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsInt24U obj = new CmsInt24U(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsInt24U d = MAPPER.readValue(json, CmsInt24U.class);
        assertEquals(obj, d);
    }
}
