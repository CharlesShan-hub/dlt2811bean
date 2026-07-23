// Auto-generated. Tests for CmsCreateDataSetResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsCreateDataSetResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsCreateDataSetResponsePDU obj = new CmsCreateDataSetResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsCreateDataSetResponsePDU obj = new CmsCreateDataSetResponsePDU(null);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsCreateDataSetResponsePDU obj = new CmsCreateDataSetResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsCreateDataSetResponsePDU d = MAPPER.readValue(json, CmsCreateDataSetResponsePDU.class);
        assertEquals(obj, d);
    }
}
