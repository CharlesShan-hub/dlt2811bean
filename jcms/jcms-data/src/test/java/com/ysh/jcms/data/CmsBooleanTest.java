// Auto-generated. Tests for CmsBoolean

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsBooleanTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsBoolean obj = new CmsBoolean();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsBoolean obj = new CmsBoolean(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsBoolean obj = new CmsBoolean(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsBoolean d = MAPPER.readValue(json, CmsBoolean.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsBoolean obj = new CmsBoolean(1);
        byte[] data = obj.encode("uper");
        CmsBoolean d = CmsBoolean.decode("uper", data);
        assertEquals(obj, d);
    }
}
