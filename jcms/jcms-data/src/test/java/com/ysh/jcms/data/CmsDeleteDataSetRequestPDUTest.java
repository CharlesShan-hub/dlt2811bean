// Auto-generated. Tests for CmsDeleteDataSetRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsDeleteDataSetRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsDeleteDataSetRequestPDU obj = new CmsDeleteDataSetRequestPDU();
        assertNull(obj.dataset_reference);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsDeleteDataSetRequestPDU obj = new CmsDeleteDataSetRequestPDU();
        obj.dataset_reference = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsDeleteDataSetRequestPDU d = MAPPER.readValue(json, CmsDeleteDataSetRequestPDU.class);
        assertEquals(obj, d);
    }
}
