// Auto-generated. Tests for CmsGetRpcInterfaceDirectoryResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetRpcInterfaceDirectoryResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetRpcInterfaceDirectoryResponsePDU obj = new CmsGetRpcInterfaceDirectoryResponsePDU();
        assertNotNull(obj.reference);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetRpcInterfaceDirectoryResponsePDU obj = new CmsGetRpcInterfaceDirectoryResponsePDU();
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetRpcInterfaceDirectoryResponsePDU d = MAPPER.readValue(json, CmsGetRpcInterfaceDirectoryResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetRpcInterfaceDirectoryResponsePDU obj = new CmsGetRpcInterfaceDirectoryResponsePDU();
        obj.more_follows = true;
        byte[] data = obj.encode("uper");
        CmsGetRpcInterfaceDirectoryResponsePDU d = CmsGetRpcInterfaceDirectoryResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
