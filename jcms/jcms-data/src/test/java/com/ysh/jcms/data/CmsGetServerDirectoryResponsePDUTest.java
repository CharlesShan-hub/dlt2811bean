// Auto-generated. Tests for CmsGetServerDirectoryResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetServerDirectoryResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetServerDirectoryResponsePDU obj = new CmsGetServerDirectoryResponsePDU();
        assertNotNull(obj.reference);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetServerDirectoryResponsePDU obj = new CmsGetServerDirectoryResponsePDU();
        obj.reference = java.util.Collections.singletonList("test");
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetServerDirectoryResponsePDU d = MAPPER.readValue(json, CmsGetServerDirectoryResponsePDU.class);
        assertEquals(obj, d);
    }
}
