// Auto-generated. Tests for CmsUtcTime

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsUtcTimeTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsUtcTime obj = new CmsUtcTime();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsUtcTime obj = new CmsUtcTime();
        obj.value = new byte[]{0x01, 0x02};
        String json = MAPPER.writeValueAsString(obj);
        CmsUtcTime d = MAPPER.readValue(json, CmsUtcTime.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsUtcTime obj = new CmsUtcTime();
        obj.value = new byte[]{0x01, 0x02};
        byte[] data = obj.encode("uper");
        CmsUtcTime d = CmsUtcTime.decode("uper", data);
        assertEquals(obj, d);
    }
}
