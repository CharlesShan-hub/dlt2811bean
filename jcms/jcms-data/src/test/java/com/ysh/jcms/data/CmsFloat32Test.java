// Auto-generated. Tests for CmsFloat32

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsFloat32Test {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsFloat32 obj = new CmsFloat32();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsFloat32 obj = new CmsFloat32();
        obj.value = new byte[]{0x01, 0x02};
        String json = MAPPER.writeValueAsString(obj);
        CmsFloat32 d = MAPPER.readValue(json, CmsFloat32.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsFloat32 obj = new CmsFloat32();
        obj.value = new byte[]{0x01, 0x02};
        byte[] data = obj.encode("uper");
        CmsFloat32 d = CmsFloat32.decode("uper", data);
        assertEquals(obj, d);
    }
}
