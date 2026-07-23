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
        if (obj.rsp_data == null) obj.rsp_data = new CmsData();
        obj.next_call_id = new byte[]{0x01, 0x02};
        String json = MAPPER.writeValueAsString(obj);
        CmsRpcCallResponsePDU d = MAPPER.readValue(json, CmsRpcCallResponsePDU.class);
        assertEquals(obj, d);
    }
}
