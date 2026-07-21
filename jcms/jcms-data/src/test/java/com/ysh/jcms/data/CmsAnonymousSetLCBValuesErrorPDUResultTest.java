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
        obj.error = 42;
        obj.log_ena = 42;
        obj.dat_set = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetLCBValuesErrorPDUResult d = MAPPER.readValue(json, CmsAnonymousSetLCBValuesErrorPDUResult.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousSetLCBValuesErrorPDUResult obj = new CmsAnonymousSetLCBValuesErrorPDUResult();
        obj.error = 42;
        obj.log_ena = 42;
        obj.dat_set = 42;
        byte[] data = obj.encode("uper");
        CmsAnonymousSetLCBValuesErrorPDUResult d = CmsAnonymousSetLCBValuesErrorPDUResult.decode("uper", data);
        assertEquals(obj, d);
    }
}
