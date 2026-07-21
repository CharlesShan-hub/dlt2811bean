// Auto-generated. Tests for CmsSelectEditSGResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSelectEditSGResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSelectEditSGResponsePDU obj = new CmsSelectEditSGResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSelectEditSGResponsePDU obj = new CmsSelectEditSGResponsePDU(null);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSelectEditSGResponsePDU obj = new CmsSelectEditSGResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSelectEditSGResponsePDU d = MAPPER.readValue(json, CmsSelectEditSGResponsePDU.class);
        assertEquals(obj, d);
    }
}
