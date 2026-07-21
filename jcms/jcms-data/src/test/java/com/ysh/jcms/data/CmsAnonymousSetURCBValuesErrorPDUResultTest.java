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
        obj.error = 42;
        obj.rpt_id = 42;
        obj.rpt_ena = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetURCBValuesErrorPDUResult d = MAPPER.readValue(json, CmsAnonymousSetURCBValuesErrorPDUResult.class);
        assertEquals(obj, d);
    }
}
