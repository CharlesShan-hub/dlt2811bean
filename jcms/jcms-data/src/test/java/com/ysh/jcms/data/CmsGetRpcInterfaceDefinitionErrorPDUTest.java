// Auto-generated. Tests for CmsGetRpcInterfaceDefinitionErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetRpcInterfaceDefinitionErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetRpcInterfaceDefinitionErrorPDU obj = new CmsGetRpcInterfaceDefinitionErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetRpcInterfaceDefinitionErrorPDU obj = new CmsGetRpcInterfaceDefinitionErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetRpcInterfaceDefinitionErrorPDU obj = new CmsGetRpcInterfaceDefinitionErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetRpcInterfaceDefinitionErrorPDU d = MAPPER.readValue(json, CmsGetRpcInterfaceDefinitionErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetRpcInterfaceDefinitionErrorPDU obj = new CmsGetRpcInterfaceDefinitionErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsGetRpcInterfaceDefinitionErrorPDU d = CmsGetRpcInterfaceDefinitionErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
