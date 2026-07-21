// Auto-generated. Tests for CmsTimeStamp

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsTimeStampTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsTimeStamp obj = new CmsTimeStamp();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsTimeStamp obj = new CmsTimeStamp();
        obj.value = new byte[]{0x01, 0x02};
        String json = MAPPER.writeValueAsString(obj);
        CmsTimeStamp d = MAPPER.readValue(json, CmsTimeStamp.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsTimeStamp obj = new CmsTimeStamp();
        obj.value = new byte[]{0x01, 0x02};
        byte[] data = obj.encode("uper");
        CmsTimeStamp d = CmsTimeStamp.decode("uper", data);
        assertEquals(obj, d);
    }
}
