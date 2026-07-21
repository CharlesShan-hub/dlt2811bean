// Auto-generated. Tests for CmsInt8U

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsInt8UTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsInt8U obj = new CmsInt8U();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsInt8U obj = new CmsInt8U(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsInt8U obj = new CmsInt8U(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsInt8U d = MAPPER.readValue(json, CmsInt8U.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsInt8U obj = new CmsInt8U(42);
        byte[] data = obj.encode("uper");
        CmsInt8U d = CmsInt8U.decode("uper", data);
        assertEquals(obj, d);
    }
}
