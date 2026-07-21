// Auto-generated. Tests for CmsAnonymousSetMSVCBValuesRequestPDUMsvcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousSetMSVCBValuesRequestPDUMsvcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousSetMSVCBValuesRequestPDUMsvcb obj = new CmsAnonymousSetMSVCBValuesRequestPDUMsvcb();
        assertNull(obj.reference);
        assertNull(obj.sv_ena);
        assertNull(obj.msv_id);
        assertNull(obj.dat_set);
        assertNull(obj.smp_mod);
        assertNull(obj.smp_rate);
        assertNull(obj.opt_flds);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousSetMSVCBValuesRequestPDUMsvcb obj = new CmsAnonymousSetMSVCBValuesRequestPDUMsvcb();
        obj.reference = "test";
        obj.sv_ena = true;
        obj.msv_id = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetMSVCBValuesRequestPDUMsvcb d = MAPPER.readValue(json, CmsAnonymousSetMSVCBValuesRequestPDUMsvcb.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousSetMSVCBValuesRequestPDUMsvcb obj = new CmsAnonymousSetMSVCBValuesRequestPDUMsvcb();
        obj.reference = "test";
        obj.sv_ena = true;
        obj.msv_id = "test";
        byte[] data = obj.encode("uper");
        CmsAnonymousSetMSVCBValuesRequestPDUMsvcb d = CmsAnonymousSetMSVCBValuesRequestPDUMsvcb.decode("uper", data);
        assertEquals(obj, d);
    }
}
