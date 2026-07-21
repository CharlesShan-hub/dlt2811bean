// Auto-generated. Tests for CmsDeleteDataSetResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsDeleteDataSetResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsDeleteDataSetResponsePDU obj = new CmsDeleteDataSetResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsDeleteDataSetResponsePDU obj = new CmsDeleteDataSetResponsePDU(null);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsDeleteDataSetResponsePDU obj = new CmsDeleteDataSetResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsDeleteDataSetResponsePDU d = MAPPER.readValue(json, CmsDeleteDataSetResponsePDU.class);
        assertEquals(obj, d);
    }
}
