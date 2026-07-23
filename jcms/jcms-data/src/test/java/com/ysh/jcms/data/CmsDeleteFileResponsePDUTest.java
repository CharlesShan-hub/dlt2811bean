// Auto-generated. Tests for CmsDeleteFileResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsDeleteFileResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsDeleteFileResponsePDU obj = new CmsDeleteFileResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsDeleteFileResponsePDU obj = new CmsDeleteFileResponsePDU(null);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsDeleteFileResponsePDU obj = new CmsDeleteFileResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsDeleteFileResponsePDU d = MAPPER.readValue(json, CmsDeleteFileResponsePDU.class);
        assertEquals(obj, d);
    }
}
