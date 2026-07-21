// Auto-generated. Tests for CmsSubReference

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSubReferenceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSubReference obj = new CmsSubReference();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSubReference obj = new CmsSubReference("hello");
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSubReference obj = new CmsSubReference("test");
        obj.value = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsSubReference d = MAPPER.readValue(json, CmsSubReference.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSubReference obj = new CmsSubReference();
        obj.value = "test";
        byte[] data = obj.encode("uper");
        CmsSubReference d = CmsSubReference.decode("uper", data);
        assertEquals(obj, d);
    }
}
