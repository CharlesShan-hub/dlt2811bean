// Auto-generated. Tests for CmsLCB

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsLCBTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsLCB obj = new CmsLCB();
        assertFalse(obj.log_ena);
        assertNull(obj.dat_set);
        assertEquals(0, obj.trg_ops);
        assertEquals(0, obj.intg_pd);
        assertNull(obj.log_ref);
        assertNull(obj.opt_flds);
        assertNull(obj.buf_tm);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsLCB obj = new CmsLCB();
        obj.log_ena = true;
        obj.dat_set = "test";
        obj.trg_ops = 1;
        obj.intg_pd = 1;
        obj.log_ref = "test";
        obj.opt_flds = 1;
        obj.buf_tm = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsLCB d = MAPPER.readValue(json, CmsLCB.class);
        assertEquals(obj, d);
    }
}
