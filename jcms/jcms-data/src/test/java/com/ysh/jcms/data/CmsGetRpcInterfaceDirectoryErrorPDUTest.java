// Auto-generated. Tests for CmsGetRpcInterfaceDirectoryErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetRpcInterfaceDirectoryErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetRpcInterfaceDirectoryErrorPDU obj = new CmsGetRpcInterfaceDirectoryErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetRpcInterfaceDirectoryErrorPDU obj = new CmsGetRpcInterfaceDirectoryErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetRpcInterfaceDirectoryErrorPDU obj = new CmsGetRpcInterfaceDirectoryErrorPDU(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetRpcInterfaceDirectoryErrorPDU d = MAPPER.readValue(json, CmsGetRpcInterfaceDirectoryErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetRpcInterfaceDirectoryErrorPDU obj = new CmsGetRpcInterfaceDirectoryErrorPDU(1);
        byte[] data = obj.encode("uper");
        CmsGetRpcInterfaceDirectoryErrorPDU d = CmsGetRpcInterfaceDirectoryErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
