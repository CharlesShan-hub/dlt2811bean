// Auto-generated. Tests for CmsGoCB

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGoCBTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGoCB obj = new CmsGoCB();
        assertFalse(obj.go_ena);
        assertNull(obj.go_id);
        assertNull(obj.dat_set);
        assertEquals(0, obj.conf_rev);
        assertFalse(obj.nds_com);
        assertNull(obj.dst_address);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGoCB obj = new CmsGoCB();
        obj.go_ena = true;
        obj.go_id = "test";
        obj.dat_set = "test";
        obj.conf_rev = 1;
        obj.nds_com = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGoCB d = MAPPER.readValue(json, CmsGoCB.class);
        assertEquals(obj, d);
    }
}
