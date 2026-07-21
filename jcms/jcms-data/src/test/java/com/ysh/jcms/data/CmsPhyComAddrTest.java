// Auto-generated. Tests for CmsPhyComAddr

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsPhyComAddrTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsPhyComAddr obj = new CmsPhyComAddr();
        assertNull(obj.addr);
        assertEquals(0, obj.priority);
        assertEquals(0, obj.vid);
        assertEquals(0, obj.appid);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsPhyComAddr obj = new CmsPhyComAddr();
        obj.addr = new byte[0];
        obj.priority = 42;
        obj.vid = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsPhyComAddr d = MAPPER.readValue(json, CmsPhyComAddr.class);
        assertEquals(obj, d);
    }
}
