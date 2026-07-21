// Auto-generated. Tests for CmsApdu

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsApduTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsApdu obj = new CmsApdu();
        assertNull(obj.apch);
        assertNull(obj.asdu);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsApdu obj = new CmsApdu();
        obj.asdu = new byte[0];
        String json = MAPPER.writeValueAsString(obj);
        CmsApdu d = MAPPER.readValue(json, CmsApdu.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsApdu obj = new CmsApdu();
        obj.asdu = new byte[0];
        byte[] data = obj.encode("uper");
        CmsApdu d = CmsApdu.decode("uper", data);
        assertEquals(obj, d);
    }
}
