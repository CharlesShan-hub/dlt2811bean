// Auto-generated. Tests for CmsURCB

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsURCBTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsURCB obj = new CmsURCB();
        assertNull(obj.rpt_id);
        assertFalse(obj.rpt_ena);
        assertNull(obj.dat_set);
        assertEquals(0, obj.conf_rev);
        assertEquals(0, obj.opt_flds);
        assertEquals(0, obj.buf_tm);
        assertEquals(0, obj.sq_num);
        assertEquals(0, obj.trg_ops);
        assertEquals(0, obj.intg_pd);
        assertFalse(obj.gi);
        assertFalse(obj.resv);
        assertNull(obj.owner);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsURCB obj = new CmsURCB();
        obj.rpt_id = "test";
        obj.rpt_ena = true;
        obj.dat_set = "test";
        obj.conf_rev = 1;
        obj.opt_flds = 1;
        obj.buf_tm = 1;
        obj.sq_num = 1;
        obj.trg_ops = 1;
        obj.intg_pd = 1;
        obj.gi = true;
        obj.resv = true;
        obj.owner = new byte[]{0x01, 0x02};
        String json = MAPPER.writeValueAsString(obj);
        CmsURCB d = MAPPER.readValue(json, CmsURCB.class);
        assertEquals(obj, d);
    }
}
