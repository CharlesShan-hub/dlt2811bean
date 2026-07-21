// Auto-generated. Tests for CmsGetLogicalNodeDirectoryResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetLogicalNodeDirectoryResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetLogicalNodeDirectoryResponsePDU obj = new CmsGetLogicalNodeDirectoryResponsePDU();
        assertNotNull(obj.reference);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetLogicalNodeDirectoryResponsePDU obj = new CmsGetLogicalNodeDirectoryResponsePDU();
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLogicalNodeDirectoryResponsePDU d = MAPPER.readValue(json, CmsGetLogicalNodeDirectoryResponsePDU.class);
        assertEquals(obj, d);
    }
}
