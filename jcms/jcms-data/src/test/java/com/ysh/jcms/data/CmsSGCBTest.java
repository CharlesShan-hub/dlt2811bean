// Auto-generated. Tests for CmsSGCB

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSGCBTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSGCB obj = new CmsSGCB();
        assertEquals(0, obj.num_of_sg);
        assertEquals(0, obj.act_sg);
        assertEquals(0, obj.edit_sg);
        assertNull(obj.t_act_edt);
        assertNull(obj.resv_tms);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSGCB obj = new CmsSGCB();
        obj.num_of_sg = 1;
        obj.act_sg = 1;
        obj.edit_sg = 1;
        obj.t_act_edt = new byte[]{0x01, 0x02};
        obj.resv_tms = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsSGCB d = MAPPER.readValue(json, CmsSGCB.class);
        assertEquals(obj, d);
    }
}
