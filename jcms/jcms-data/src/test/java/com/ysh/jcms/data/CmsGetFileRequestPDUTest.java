// Auto-generated. Tests for CmsGetFileRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetFileRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetFileRequestPDU obj = new CmsGetFileRequestPDU();
        assertNull(obj.filename);
        assertEquals(0, obj.start_position);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetFileRequestPDU obj = new CmsGetFileRequestPDU();
        obj.filename = "test";
        obj.start_position = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetFileRequestPDU d = MAPPER.readValue(json, CmsGetFileRequestPDU.class);
        assertEquals(obj, d);
    }
}
