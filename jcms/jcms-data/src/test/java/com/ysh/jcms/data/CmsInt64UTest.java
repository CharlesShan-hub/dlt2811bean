// Auto-generated. Tests for CmsInt64U

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsInt64UTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsInt64U obj = new CmsInt64U();
        assertEquals(0L, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsInt64U obj = new CmsInt64U(42L);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsInt64U obj = new CmsInt64U(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsInt64U d = MAPPER.readValue(json, CmsInt64U.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsInt64U obj = new CmsInt64U(1);
        byte[] data = obj.encode("uper");
        CmsInt64U d = CmsInt64U.decode("uper", data);
        assertEquals(obj, d);
    }
}
