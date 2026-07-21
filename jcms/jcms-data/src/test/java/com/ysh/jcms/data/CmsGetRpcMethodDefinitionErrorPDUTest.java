// Auto-generated. Tests for CmsGetRpcMethodDefinitionErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetRpcMethodDefinitionErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetRpcMethodDefinitionErrorPDU obj = new CmsGetRpcMethodDefinitionErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetRpcMethodDefinitionErrorPDU obj = new CmsGetRpcMethodDefinitionErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetRpcMethodDefinitionErrorPDU obj = new CmsGetRpcMethodDefinitionErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetRpcMethodDefinitionErrorPDU d = MAPPER.readValue(json, CmsGetRpcMethodDefinitionErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetRpcMethodDefinitionErrorPDU obj = new CmsGetRpcMethodDefinitionErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsGetRpcMethodDefinitionErrorPDU d = CmsGetRpcMethodDefinitionErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
