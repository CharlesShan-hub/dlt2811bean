// Auto-generated. Tests for CmsQueryLogByTimeRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsQueryLogByTimeRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsQueryLogByTimeRequestPDU obj = new CmsQueryLogByTimeRequestPDU();
        assertNull(obj.log_reference);
        assertNull(obj.start_time);
        assertNull(obj.stop_time);
        assertNull(obj.entry_after);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsQueryLogByTimeRequestPDU obj = new CmsQueryLogByTimeRequestPDU();
        obj.log_reference = "test";
        obj.start_time = new byte[]{0x01, 0x02};
        obj.stop_time = new byte[]{0x01, 0x02};
        obj.entry_after = new byte[]{0x01, 0x02};
        String json = MAPPER.writeValueAsString(obj);
        CmsQueryLogByTimeRequestPDU d = MAPPER.readValue(json, CmsQueryLogByTimeRequestPDU.class);
        assertEquals(obj, d);
    }
}
