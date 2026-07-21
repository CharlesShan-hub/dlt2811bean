// Auto-generated. Tests for CmsGetRpcInterfaceDefinitionRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetRpcInterfaceDefinitionRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetRpcInterfaceDefinitionRequestPDU obj = new CmsGetRpcInterfaceDefinitionRequestPDU();
        assertNull(obj._interface);
        assertNull(obj.reference_after);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetRpcInterfaceDefinitionRequestPDU obj = new CmsGetRpcInterfaceDefinitionRequestPDU();
        obj._interface = "test";
        obj.reference_after = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetRpcInterfaceDefinitionRequestPDU d = MAPPER.readValue(json, CmsGetRpcInterfaceDefinitionRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetRpcInterfaceDefinitionRequestPDU obj = new CmsGetRpcInterfaceDefinitionRequestPDU();
        obj._interface = "test";
        obj.reference_after = "test";
        byte[] data = obj.encode("uper");
        CmsGetRpcInterfaceDefinitionRequestPDU d = CmsGetRpcInterfaceDefinitionRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
