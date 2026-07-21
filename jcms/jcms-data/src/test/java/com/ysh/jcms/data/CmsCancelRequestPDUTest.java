// Auto-generated. Tests for CmsCancelRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsCancelRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsCancelRequestPDU obj = new CmsCancelRequestPDU();
        assertNull(obj.reference);
        assertNull(obj.ctl_val);
        assertNull(obj.oper_tm);
        assertNull(obj.origin);
        assertEquals(0, obj.ctl_num);
        assertNull(obj.t);
        assertFalse(obj.test);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsCancelRequestPDU obj = new CmsCancelRequestPDU();
        obj.reference = "test";
        obj.oper_tm = new byte[0];
        obj.ctl_num = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsCancelRequestPDU d = MAPPER.readValue(json, CmsCancelRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsCancelRequestPDU obj = new CmsCancelRequestPDU();
        obj.reference = "test";
        obj.oper_tm = new byte[0];
        obj.ctl_num = 42;
        byte[] data = obj.encode("uper");
        CmsCancelRequestPDU d = CmsCancelRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
