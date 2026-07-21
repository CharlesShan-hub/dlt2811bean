// Auto-generated. Tests for CmsDeleteFileRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsDeleteFileRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsDeleteFileRequestPDU obj = new CmsDeleteFileRequestPDU();
        assertNull(obj.filename);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsDeleteFileRequestPDU obj = new CmsDeleteFileRequestPDU();
        obj.filename = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsDeleteFileRequestPDU d = MAPPER.readValue(json, CmsDeleteFileRequestPDU.class);
        assertEquals(obj, d);
    }
}
