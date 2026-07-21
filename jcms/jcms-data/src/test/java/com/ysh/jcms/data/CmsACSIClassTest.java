// Auto-generated. Tests for CmsACSIClass

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsACSIClassTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsACSIClass obj = new CmsACSIClass();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsACSIClass obj = new CmsACSIClass(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsACSIClass obj = new CmsACSIClass(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsACSIClass d = MAPPER.readValue(json, CmsACSIClass.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsACSIClass obj = new CmsACSIClass(42);
        byte[] data = obj.encode("uper");
        CmsACSIClass d = CmsACSIClass.decode("uper", data);
        assertEquals(obj, d);
    }
}
