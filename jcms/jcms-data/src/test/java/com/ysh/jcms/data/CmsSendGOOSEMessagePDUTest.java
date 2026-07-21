// Auto-generated. Tests for CmsSendGOOSEMessagePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSendGOOSEMessagePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSendGOOSEMessagePDU obj = new CmsSendGOOSEMessagePDU();
        assertNull(obj.go_id);
        assertNull(obj.dat_set);
        assertNull(obj.go_ref);
        assertNull(obj.t);
        assertEquals(0, obj.st_num);
        assertEquals(0, obj.sq_num);
        assertFalse(obj.simulation);
        assertEquals(0, obj.conf_rev);
        assertFalse(obj.nds_com);
        assertNotNull(obj.data);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSendGOOSEMessagePDU obj = new CmsSendGOOSEMessagePDU();
        obj.go_id = "test";
        obj.dat_set = "test";
        obj.go_ref = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsSendGOOSEMessagePDU d = MAPPER.readValue(json, CmsSendGOOSEMessagePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSendGOOSEMessagePDU obj = new CmsSendGOOSEMessagePDU();
        obj.go_id = "test";
        obj.dat_set = "test";
        obj.go_ref = "test";
        byte[] data = obj.encode("uper");
        CmsSendGOOSEMessagePDU d = CmsSendGOOSEMessagePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
