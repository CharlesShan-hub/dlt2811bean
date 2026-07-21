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
        if (obj.apch == null) obj.apch = new CmsApch();
        obj.asdu = new byte[]{0x01, 0x02};
        String json = MAPPER.writeValueAsString(obj);
        CmsApdu d = MAPPER.readValue(json, CmsApdu.class);
        assertEquals(obj, d);
    }
}
