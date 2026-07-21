// Auto-generated. Tests for CmsRpcCallRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsRpcCallRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsRpcCallRequestPDU obj = new CmsRpcCallRequestPDU();
        assertNull(obj.method);
        assertNull(obj.req);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsRpcCallRequestPDU obj = new CmsRpcCallRequestPDU();
        obj.method = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsRpcCallRequestPDU d = MAPPER.readValue(json, CmsRpcCallRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsRpcCallRequestPDU obj = new CmsRpcCallRequestPDU();
        obj.method = "test";
        byte[] data = obj.encode("uper");
        CmsRpcCallRequestPDU d = CmsRpcCallRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
