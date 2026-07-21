// Auto-generated. Tests for CmsGetFileResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetFileResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetFileResponsePDU obj = new CmsGetFileResponsePDU();
        assertNull(obj.file_data);
        assertFalse(obj.end_of_file);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetFileResponsePDU obj = new CmsGetFileResponsePDU();
        obj.file_data = new byte[0];
        obj.end_of_file = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetFileResponsePDU d = MAPPER.readValue(json, CmsGetFileResponsePDU.class);
        assertEquals(obj, d);
    }
}
