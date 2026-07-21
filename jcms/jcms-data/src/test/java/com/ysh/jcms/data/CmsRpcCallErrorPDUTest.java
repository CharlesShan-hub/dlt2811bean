// Auto-generated. Tests for CmsRpcCallErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsRpcCallErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsRpcCallErrorPDU obj = new CmsRpcCallErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsRpcCallErrorPDU obj = new CmsRpcCallErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsRpcCallErrorPDU obj = new CmsRpcCallErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsRpcCallErrorPDU d = MAPPER.readValue(json, CmsRpcCallErrorPDU.class);
        assertEquals(obj, d);
    }
}
