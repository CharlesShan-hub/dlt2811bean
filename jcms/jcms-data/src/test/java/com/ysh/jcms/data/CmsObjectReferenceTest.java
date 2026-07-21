// Auto-generated. Tests for CmsObjectReference

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsObjectReferenceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsObjectReference obj = new CmsObjectReference();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsObjectReference obj = new CmsObjectReference("hello");
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsObjectReference obj = new CmsObjectReference("test");
        obj.value = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsObjectReference d = MAPPER.readValue(json, CmsObjectReference.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsObjectReference obj = new CmsObjectReference();
        obj.value = "test";
        byte[] data = obj.encode("uper");
        CmsObjectReference d = CmsObjectReference.decode("uper", data);
        assertEquals(obj, d);
    }
}
