// Auto-generated. Tests for CmsSelectEditSGRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSelectEditSGRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSelectEditSGRequestPDU obj = new CmsSelectEditSGRequestPDU();
        assertNull(obj.sgcb_reference);
        assertEquals(0, obj.setting_group_number);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSelectEditSGRequestPDU obj = new CmsSelectEditSGRequestPDU();
        obj.sgcb_reference = "test";
        obj.setting_group_number = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsSelectEditSGRequestPDU d = MAPPER.readValue(json, CmsSelectEditSGRequestPDU.class);
        assertEquals(obj, d);
    }
}
