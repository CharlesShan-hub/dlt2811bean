// Auto-generated. Tests for CmsCancelResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsCancelResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsCancelResponsePDU obj = new CmsCancelResponsePDU();
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
        CmsCancelResponsePDU obj = new CmsCancelResponsePDU();
        obj.reference = "test";
        obj.oper_tm = new byte[0];
        obj.ctl_num = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsCancelResponsePDU d = MAPPER.readValue(json, CmsCancelResponsePDU.class);
        assertEquals(obj, d);
    }
}
