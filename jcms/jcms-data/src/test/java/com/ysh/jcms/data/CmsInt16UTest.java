// Auto-generated. Tests for CmsInt16U

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsInt16UTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsInt16U obj = new CmsInt16U();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsInt16U obj = new CmsInt16U(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsInt16U obj = new CmsInt16U(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsInt16U d = MAPPER.readValue(json, CmsInt16U.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsInt16U obj = new CmsInt16U(1);
        byte[] data = obj.encode("uper");
        CmsInt16U d = CmsInt16U.decode("uper", data);
        assertEquals(obj, d);
    }
}
