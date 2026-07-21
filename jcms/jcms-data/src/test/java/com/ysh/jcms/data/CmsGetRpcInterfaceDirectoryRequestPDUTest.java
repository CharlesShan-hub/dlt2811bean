// Auto-generated. Tests for CmsGetRpcInterfaceDirectoryRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetRpcInterfaceDirectoryRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetRpcInterfaceDirectoryRequestPDU obj = new CmsGetRpcInterfaceDirectoryRequestPDU();
        assertNull(obj.reference_after);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetRpcInterfaceDirectoryRequestPDU obj = new CmsGetRpcInterfaceDirectoryRequestPDU();
        obj.reference_after = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetRpcInterfaceDirectoryRequestPDU d = MAPPER.readValue(json, CmsGetRpcInterfaceDirectoryRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetRpcInterfaceDirectoryRequestPDU obj = new CmsGetRpcInterfaceDirectoryRequestPDU();
        obj.reference_after = "test";
        byte[] data = obj.encode("uper");
        CmsGetRpcInterfaceDirectoryRequestPDU d = CmsGetRpcInterfaceDirectoryRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
