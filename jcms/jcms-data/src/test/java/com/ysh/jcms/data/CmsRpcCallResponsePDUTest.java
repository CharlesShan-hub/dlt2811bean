// Auto-generated. Tests for CmsRpcCallResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsRpcCallResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsRpcCallResponsePDU obj = new CmsRpcCallResponsePDU();
        assertNull(obj.rsp_data);
        assertNull(obj.next_call_id);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsRpcCallResponsePDU obj = new CmsRpcCallResponsePDU();
        obj.next_call_id = new byte[0];
        String json = MAPPER.writeValueAsString(obj);
        CmsRpcCallResponsePDU d = MAPPER.readValue(json, CmsRpcCallResponsePDU.class);
        assertEquals(obj, d);
    }
}
