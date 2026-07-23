// Auto-generated. Tests for CmsAnonymousSetURCBValuesErrorPDUResult

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousSetURCBValuesErrorPDUResultTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousSetURCBValuesErrorPDUResult obj = new CmsAnonymousSetURCBValuesErrorPDUResult();
        assertNull(obj.error);
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
        CmsAnonymousSetURCBValuesErrorPDUResult obj = new CmsAnonymousSetURCBValuesErrorPDUResult();
        obj.error = 1;
        obj.rpt_id = 1;
        obj.rpt_ena = 1;
        obj.dat_set = 1;
        obj.opt_flds = 1;
        obj.buf_tm = 1;
        obj.trg_ops = 1;
        obj.intg_pd = 1;
        obj.gi = 1;
        obj.resv = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetURCBValuesErrorPDUResult d = MAPPER.readValue(json, CmsAnonymousSetURCBValuesErrorPDUResult.class);
        assertEquals(obj, d);
    }
}
