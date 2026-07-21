// Auto-generated. Tests for CmsFloat64

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsFloat64Test {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsFloat64 obj = new CmsFloat64();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsFloat64 obj = new CmsFloat64();
        obj.value = new byte[]{0x01, 0x02};
        String json = MAPPER.writeValueAsString(obj);
        CmsFloat64 d = MAPPER.readValue(json, CmsFloat64.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsFloat64 obj = new CmsFloat64();
        obj.value = new byte[]{0x01, 0x02};
        byte[] data = obj.encode("uper");
        CmsFloat64 d = CmsFloat64.decode("uper", data);
        assertEquals(obj, d);
    }
}
