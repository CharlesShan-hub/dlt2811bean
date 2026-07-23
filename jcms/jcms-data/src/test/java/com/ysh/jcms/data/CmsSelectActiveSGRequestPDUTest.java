// Auto-generated. Tests for CmsSelectActiveSGRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSelectActiveSGRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSelectActiveSGRequestPDU obj = new CmsSelectActiveSGRequestPDU();
        assertNull(obj.sgcb_reference);
        assertEquals(0, obj.setting_group_number);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSelectActiveSGRequestPDU obj = new CmsSelectActiveSGRequestPDU();
        obj.sgcb_reference = "test";
        obj.setting_group_number = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsSelectActiveSGRequestPDU d = MAPPER.readValue(json, CmsSelectActiveSGRequestPDU.class);
        assertEquals(obj, d);
    }
}
