// Auto-generated. Tests for CmsSendMSVMessagePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSendMSVMessagePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSendMSVMessagePDU obj = new CmsSendMSVMessagePDU();
        assertNull(obj.msv_id);
        assertNull(obj.dat_set);
        assertEquals(0, obj.smp_cnt);
        assertEquals(0, obj.conf_rev);
        assertNull(obj.ref_tm);
        assertEquals(0, obj.smp_synch);
        assertNull(obj.smp_rate);
        assertFalse(obj.simulation);
        assertNotNull(obj.sample);
        assertNull(obj.smp_mod);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSendMSVMessagePDU obj = new CmsSendMSVMessagePDU();
        obj.msv_id = "test";
        obj.dat_set = "test";
        obj.smp_cnt = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsSendMSVMessagePDU d = MAPPER.readValue(json, CmsSendMSVMessagePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSendMSVMessagePDU obj = new CmsSendMSVMessagePDU();
        obj.msv_id = "test";
        obj.dat_set = "test";
        obj.smp_cnt = 42;
        byte[] data = obj.encode("uper");
        CmsSendMSVMessagePDU d = CmsSendMSVMessagePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
