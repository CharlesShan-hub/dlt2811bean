// Auto-generated. Tests for CmsGetRpcMethodDirectoryRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetRpcMethodDirectoryRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetRpcMethodDirectoryRequestPDU obj = new CmsGetRpcMethodDirectoryRequestPDU();
        assertNull(obj._interface);
        assertNull(obj.reference_after);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetRpcMethodDirectoryRequestPDU obj = new CmsGetRpcMethodDirectoryRequestPDU();
        obj._interface = "test";
        obj.reference_after = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetRpcMethodDirectoryRequestPDU d = MAPPER.readValue(json, CmsGetRpcMethodDirectoryRequestPDU.class);
        assertEquals(obj, d);
    }
}
