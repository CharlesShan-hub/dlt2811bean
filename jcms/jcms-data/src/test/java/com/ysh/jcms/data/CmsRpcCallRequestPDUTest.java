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
        if (obj.req == null) obj.req = new CmsRpcCallRequestPDUReq();
        String json = MAPPER.writeValueAsString(obj);
        CmsRpcCallRequestPDU d = MAPPER.readValue(json, CmsRpcCallRequestPDU.class);
        assertEquals(obj, d);
    }
}
