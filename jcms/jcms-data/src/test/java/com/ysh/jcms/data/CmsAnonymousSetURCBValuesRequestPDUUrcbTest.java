// Auto-generated. Tests for CmsAnonymousSetURCBValuesRequestPDUUrcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousSetURCBValuesRequestPDUUrcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousSetURCBValuesRequestPDUUrcb obj = new CmsAnonymousSetURCBValuesRequestPDUUrcb();
        assertNull(obj.reference);
        assertNull(obj.rpt_id);
        assertNull(obj.rpt_ena);
        assertNull(obj.dat_set);
        assertNull(obj.opt_flds);
        assertNull(obj.buf_tm);
        assertNull(obj.trg_ops);
        assertNull(obj.intg_pd);
        assertNull(obj.gi);
        assertNull(obj.resv);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousSetURCBValuesRequestPDUUrcb obj = new CmsAnonymousSetURCBValuesRequestPDUUrcb();
        obj.reference = "test";
        obj.rpt_id = "test";
        obj.rpt_ena = true;
        obj.dat_set = "test";
        obj.opt_flds = 1;
        obj.buf_tm = 1;
        obj.trg_ops = 1;
        obj.intg_pd = 1;
        obj.gi = true;
        obj.resv = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetURCBValuesRequestPDUUrcb d = MAPPER.readValue(json, CmsAnonymousSetURCBValuesRequestPDUUrcb.class);
        assertEquals(obj, d);
    }
}
