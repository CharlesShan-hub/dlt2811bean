// Auto-generated. Tests for CmsCancelErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsCancelErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsCancelErrorPDU obj = new CmsCancelErrorPDU();
        assertNull(obj.reference);
        assertNull(obj.ctl_val);
        assertNull(obj.oper_tm);
        assertNull(obj.origin);
        assertEquals(0, obj.ctl_num);
        assertNull(obj.t);
        assertFalse(obj.test);
        assertEquals(0, obj.add_cause);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsCancelErrorPDU obj = new CmsCancelErrorPDU();
        obj.reference = "test";
        obj.oper_tm = new byte[0];
        obj.ctl_num = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsCancelErrorPDU d = MAPPER.readValue(json, CmsCancelErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsCancelErrorPDU obj = new CmsCancelErrorPDU();
        obj.reference = "test";
        obj.oper_tm = new byte[0];
        obj.ctl_num = 42;
        byte[] data = obj.encode("uper");
        CmsCancelErrorPDU d = CmsCancelErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
