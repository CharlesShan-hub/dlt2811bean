// Auto-generated. Tests for CmsAnonymousSetLCBValuesErrorPDUResult

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousSetLCBValuesErrorPDUResultTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousSetLCBValuesErrorPDUResult obj = new CmsAnonymousSetLCBValuesErrorPDUResult();
        assertNull(obj.error);
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
        CmsAnonymousSetLCBValuesErrorPDUResult obj = new CmsAnonymousSetLCBValuesErrorPDUResult();
        obj.error = 1;
        obj.log_ena = 1;
        obj.dat_set = 1;
        obj.trg_ops = 1;
        obj.intg_pd = 1;
        obj.log_ref = 1;
        obj.opt_flds = 1;
        obj.buf_tm = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetLCBValuesErrorPDUResult d = MAPPER.readValue(json, CmsAnonymousSetLCBValuesErrorPDUResult.class);
        assertEquals(obj, d);
    }
}
