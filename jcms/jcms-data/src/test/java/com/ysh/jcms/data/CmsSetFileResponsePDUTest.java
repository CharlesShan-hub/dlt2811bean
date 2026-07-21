// Auto-generated. Tests for CmsSetFileResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetFileResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetFileResponsePDU obj = new CmsSetFileResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSetFileResponsePDU obj = new CmsSetFileResponsePDU(null);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetFileResponsePDU obj = new CmsSetFileResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetFileResponsePDU d = MAPPER.readValue(json, CmsSetFileResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetFileResponsePDU obj = new CmsSetFileResponsePDU();
        byte[] data = obj.encode("uper");
        CmsSetFileResponsePDU d = CmsSetFileResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
