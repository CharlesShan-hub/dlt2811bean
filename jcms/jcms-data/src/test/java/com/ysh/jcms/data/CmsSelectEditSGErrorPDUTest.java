// Auto-generated. Tests for CmsSelectEditSGErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSelectEditSGErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSelectEditSGErrorPDU obj = new CmsSelectEditSGErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSelectEditSGErrorPDU obj = new CmsSelectEditSGErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSelectEditSGErrorPDU obj = new CmsSelectEditSGErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsSelectEditSGErrorPDU d = MAPPER.readValue(json, CmsSelectEditSGErrorPDU.class);
        assertEquals(obj, d);
    }
}
