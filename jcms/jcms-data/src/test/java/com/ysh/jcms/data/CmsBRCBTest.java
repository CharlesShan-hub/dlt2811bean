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
        String json = MAPPER.writeValueAsString(obj);
        CmsBRCB d = MAPPER.readValue(json, CmsBRCB.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsBRCB obj = new CmsBRCB();
        obj.rpt_id = "test";
        obj.rpt_ena = true;
        obj.dat_set = "test";
        byte[] data = obj.encode("uper");
        CmsBRCB d = CmsBRCB.decode("uper", data);
        assertEquals(obj, d);
    }
}
