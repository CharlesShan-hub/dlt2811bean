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
        obj.error = 1;
        obj.sv_ena = 1;
        obj.msv_id = 1;
        obj.dat_set = 1;
        obj.smp_mod = 1;
        obj.smp_rate = 1;
        obj.opt_flds = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetMSVCBValuesErrorPDUResult d = MAPPER.readValue(json, CmsAnonymousSetMSVCBValuesErrorPDUResult.class);
        assertEquals(obj, d);
    }
}
