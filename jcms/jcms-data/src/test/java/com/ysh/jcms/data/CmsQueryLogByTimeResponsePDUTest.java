// Auto-generated. Tests for CmsQueryLogByTimeResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsQueryLogByTimeResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsQueryLogByTimeResponsePDU obj = new CmsQueryLogByTimeResponsePDU();
        assertNotNull(obj.log_entry);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsQueryLogByTimeResponsePDU obj = new CmsQueryLogByTimeResponsePDU();
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsQueryLogByTimeResponsePDU d = MAPPER.readValue(json, CmsQueryLogByTimeResponsePDU.class);
        assertEquals(obj, d);
    }
}
