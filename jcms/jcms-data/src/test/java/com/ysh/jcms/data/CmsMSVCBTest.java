// Auto-generated. Tests for CmsMSVCB

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsMSVCBTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsMSVCB obj = new CmsMSVCB();
        assertFalse(obj.sv_ena);
        assertNull(obj.msv_id);
        assertNull(obj.dat_set);
        assertEquals(0, obj.conf_rev);
        assertNull(obj.smp_mod);
        assertEquals(0, obj.smp_rate);
        assertEquals(0, obj.opt_flds);
        assertNull(obj.dst_address);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsMSVCB obj = new CmsMSVCB();
        obj.sv_ena = true;
        obj.msv_id = "test";
        obj.dat_set = "test";
        obj.conf_rev = 1;
        obj.smp_mod = 1;
        obj.smp_rate = 1;
        obj.opt_flds = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsMSVCB d = MAPPER.readValue(json, CmsMSVCB.class);
        assertEquals(obj, d);
    }
}
