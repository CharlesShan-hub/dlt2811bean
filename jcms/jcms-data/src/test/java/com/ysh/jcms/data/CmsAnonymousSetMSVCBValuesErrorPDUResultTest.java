// Auto-generated. Tests for CmsAnonymousSetMSVCBValuesErrorPDUResult

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousSetMSVCBValuesErrorPDUResultTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousSetMSVCBValuesErrorPDUResult obj = new CmsAnonymousSetMSVCBValuesErrorPDUResult();
        assertNull(obj.error);
        assertNull(obj.sv_ena);
        assertNull(obj.msv_id);
        assertNull(obj.dat_set);
        assertNull(obj.smp_mod);
        assertNull(obj.smp_rate);
        assertNull(obj.opt_flds);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousSetMSVCBValuesErrorPDUResult obj = new CmsAnonymousSetMSVCBValuesErrorPDUResult();
        obj.error = 42;
        obj.sv_ena = 42;
        obj.msv_id = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetMSVCBValuesErrorPDUResult d = MAPPER.readValue(json, CmsAnonymousSetMSVCBValuesErrorPDUResult.class);
        assertEquals(obj, d);
    }
}
