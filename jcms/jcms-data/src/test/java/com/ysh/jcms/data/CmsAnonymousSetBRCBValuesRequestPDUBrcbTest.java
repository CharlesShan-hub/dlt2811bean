// Auto-generated. Tests for CmsAnonymousSetBRCBValuesRequestPDUBrcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousSetBRCBValuesRequestPDUBrcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousSetBRCBValuesRequestPDUBrcb obj = new CmsAnonymousSetBRCBValuesRequestPDUBrcb();
        assertNull(obj.reference);
        assertNull(obj.rpt_id);
        assertNull(obj.rpt_ena);
        assertNull(obj.dat_set);
        assertNull(obj.opt_flds);
        assertNull(obj.buf_tm);
        assertNull(obj.trg_ops);
        assertNull(obj.intg_pd);
        assertNull(obj.gi);
        assertNull(obj.purge_buf);
        assertNull(obj.entry_id);
        assertNull(obj.resv_tms);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousSetBRCBValuesRequestPDUBrcb obj = new CmsAnonymousSetBRCBValuesRequestPDUBrcb();
        obj.reference = "test";
        obj.rpt_id = "test";
        obj.rpt_ena = true;
        obj.dat_set = "test";
        obj.opt_flds = 1;
        obj.buf_tm = 1;
        obj.trg_ops = 1;
        obj.intg_pd = 1;
        obj.gi = true;
        obj.purge_buf = true;
        obj.entry_id = new byte[]{0x01, 0x02};
        obj.resv_tms = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetBRCBValuesRequestPDUBrcb d = MAPPER.readValue(json, CmsAnonymousSetBRCBValuesRequestPDUBrcb.class);
        assertEquals(obj, d);
    }
}
