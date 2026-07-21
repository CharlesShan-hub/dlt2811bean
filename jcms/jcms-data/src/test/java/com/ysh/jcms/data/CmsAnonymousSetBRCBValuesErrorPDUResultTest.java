// Auto-generated. Tests for CmsAnonymousSetBRCBValuesErrorPDUResult

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousSetBRCBValuesErrorPDUResultTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousSetBRCBValuesErrorPDUResult obj = new CmsAnonymousSetBRCBValuesErrorPDUResult();
        assertNull(obj.error);
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
        CmsAnonymousSetBRCBValuesErrorPDUResult obj = new CmsAnonymousSetBRCBValuesErrorPDUResult();
        obj.error = 42;
        obj.rpt_id = 42;
        obj.rpt_ena = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetBRCBValuesErrorPDUResult d = MAPPER.readValue(json, CmsAnonymousSetBRCBValuesErrorPDUResult.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousSetBRCBValuesErrorPDUResult obj = new CmsAnonymousSetBRCBValuesErrorPDUResult();
        obj.error = 42;
        obj.rpt_id = 42;
        obj.rpt_ena = 42;
        byte[] data = obj.encode("uper");
        CmsAnonymousSetBRCBValuesErrorPDUResult d = CmsAnonymousSetBRCBValuesErrorPDUResult.decode("uper", data);
        assertEquals(obj, d);
    }
}
