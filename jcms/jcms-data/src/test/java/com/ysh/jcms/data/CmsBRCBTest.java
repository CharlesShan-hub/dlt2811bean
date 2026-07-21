// Auto-generated. Tests for CmsBRCB

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsBRCBTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsBRCB obj = new CmsBRCB();
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
        assertFalse(obj.purge_buf);
        assertNull(obj.entry_id);
        assertNull(obj.time_of_entry);
        assertNull(obj.resv_tms);
        assertNull(obj.owner);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsBRCB obj = new CmsBRCB();
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
        obj.purge_buf = true;
        obj.entry_id = new byte[]{0x01, 0x02};
        obj.time_of_entry = new byte[]{0x01, 0x02};
        obj.resv_tms = 1;
        obj.owner = new byte[]{0x01, 0x02};
        String json = MAPPER.writeValueAsString(obj);
        CmsBRCB d = MAPPER.readValue(json, CmsBRCB.class);
        assertEquals(obj, d);
    }
}
