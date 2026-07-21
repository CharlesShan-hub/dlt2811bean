// Auto-generated. Tests for CmsCommandTerminationRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsCommandTerminationRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsCommandTerminationRequestPDU obj = new CmsCommandTerminationRequestPDU();
        assertNull(obj.reference);
        assertNull(obj.ctl_val);
        assertNull(obj.oper_tm);
        assertNull(obj.origin);
        assertEquals(0, obj.ctl_num);
        assertNull(obj.t);
        assertFalse(obj.test);
        assertEquals(0, obj.check);
        assertNull(obj.add_cause);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsCommandTerminationRequestPDU obj = new CmsCommandTerminationRequestPDU();
        obj.reference = "test";
        obj.oper_tm = new byte[0];
        obj.ctl_num = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsCommandTerminationRequestPDU d = MAPPER.readValue(json, CmsCommandTerminationRequestPDU.class);
        assertEquals(obj, d);
    }
}
