// Auto-generated. Tests for CmsAnonymousSetLCBValuesRequestPDULcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousSetLCBValuesRequestPDULcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousSetLCBValuesRequestPDULcb obj = new CmsAnonymousSetLCBValuesRequestPDULcb();
        assertNull(obj.reference);
        assertNull(obj.log_ena);
        assertNull(obj.dat_set);
        assertNull(obj.trg_ops);
        assertNull(obj.intg_pd);
        assertNull(obj.log_ref);
        assertNull(obj.opt_flds);
        assertNull(obj.buf_tm);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousSetLCBValuesRequestPDULcb obj = new CmsAnonymousSetLCBValuesRequestPDULcb();
        obj.reference = "test";
        obj.log_ena = true;
        obj.dat_set = "test";
        obj.trg_ops = 1;
        obj.intg_pd = 1;
        obj.log_ref = "test";
        obj.opt_flds = 1;
        obj.buf_tm = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetLCBValuesRequestPDULcb d = MAPPER.readValue(json, CmsAnonymousSetLCBValuesRequestPDULcb.class);
        assertEquals(obj, d);
    }
}
