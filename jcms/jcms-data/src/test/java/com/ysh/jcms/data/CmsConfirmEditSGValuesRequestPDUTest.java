// Auto-generated. Tests for CmsConfirmEditSGValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsConfirmEditSGValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsConfirmEditSGValuesRequestPDU obj = new CmsConfirmEditSGValuesRequestPDU();
        assertNull(obj.sgcb_reference);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsConfirmEditSGValuesRequestPDU obj = new CmsConfirmEditSGValuesRequestPDU();
        obj.sgcb_reference = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsConfirmEditSGValuesRequestPDU d = MAPPER.readValue(json, CmsConfirmEditSGValuesRequestPDU.class);
        assertEquals(obj, d);
    }
}
