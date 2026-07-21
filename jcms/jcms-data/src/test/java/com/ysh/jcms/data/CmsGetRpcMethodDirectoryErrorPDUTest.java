// Auto-generated. Tests for CmsGetRpcMethodDirectoryErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetRpcMethodDirectoryErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetRpcMethodDirectoryErrorPDU obj = new CmsGetRpcMethodDirectoryErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetRpcMethodDirectoryErrorPDU obj = new CmsGetRpcMethodDirectoryErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetRpcMethodDirectoryErrorPDU obj = new CmsGetRpcMethodDirectoryErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetRpcMethodDirectoryErrorPDU d = MAPPER.readValue(json, CmsGetRpcMethodDirectoryErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetRpcMethodDirectoryErrorPDU obj = new CmsGetRpcMethodDirectoryErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsGetRpcMethodDirectoryErrorPDU d = CmsGetRpcMethodDirectoryErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
