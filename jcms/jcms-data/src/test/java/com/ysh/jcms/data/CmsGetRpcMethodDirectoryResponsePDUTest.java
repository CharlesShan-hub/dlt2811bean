// Auto-generated. Tests for CmsGetRpcMethodDirectoryResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetRpcMethodDirectoryResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetRpcMethodDirectoryResponsePDU obj = new CmsGetRpcMethodDirectoryResponsePDU();
        assertNotNull(obj.reference);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetRpcMethodDirectoryResponsePDU obj = new CmsGetRpcMethodDirectoryResponsePDU();
        obj.reference = java.util.Collections.singletonList("test");
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetRpcMethodDirectoryResponsePDU d = MAPPER.readValue(json, CmsGetRpcMethodDirectoryResponsePDU.class);
        assertEquals(obj, d);
    }
}
