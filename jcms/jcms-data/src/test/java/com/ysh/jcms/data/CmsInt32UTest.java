// Auto-generated. Tests for CmsInt32U

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsInt32UTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsInt32U obj = new CmsInt32U();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsInt32U obj = new CmsInt32U(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsInt32U obj = new CmsInt32U(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsInt32U d = MAPPER.readValue(json, CmsInt32U.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsInt32U obj = new CmsInt32U(1);
        byte[] data = obj.encode("uper");
        CmsInt32U d = CmsInt32U.decode("uper", data);
        assertEquals(obj, d);
    }
}
