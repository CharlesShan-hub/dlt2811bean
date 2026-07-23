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
        if (obj.ctl_val == null) obj.ctl_val = new CmsData();
        obj.oper_tm = new byte[]{0x01, 0x02};
        if (obj.origin == null) obj.origin = new CmsOriginator();
        obj.ctl_num = 1;
        obj.t = new byte[]{0x01, 0x02};
        obj.test = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsCancelRequestPDU d = MAPPER.readValue(json, CmsCancelRequestPDU.class);
        assertEquals(obj, d);
    }
}
