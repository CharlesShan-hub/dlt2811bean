// Auto-generated. Tests for CmsObjectName

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsObjectNameTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsObjectName obj = new CmsObjectName();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsObjectName obj = new CmsObjectName("hello");
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsObjectName obj = new CmsObjectName("test");
        obj.value = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsObjectName d = MAPPER.readValue(json, CmsObjectName.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsObjectName obj = new CmsObjectName();
        obj.value = "test";
        byte[] data = obj.encode("uper");
        CmsObjectName d = CmsObjectName.decode("uper", data);
        assertEquals(obj, d);
    }
}
