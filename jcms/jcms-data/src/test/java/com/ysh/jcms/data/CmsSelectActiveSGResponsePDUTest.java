// Auto-generated. Tests for CmsSelectActiveSGResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSelectActiveSGResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSelectActiveSGResponsePDU obj = new CmsSelectActiveSGResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSelectActiveSGResponsePDU obj = new CmsSelectActiveSGResponsePDU(null);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSelectActiveSGResponsePDU obj = new CmsSelectActiveSGResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSelectActiveSGResponsePDU d = MAPPER.readValue(json, CmsSelectActiveSGResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSelectActiveSGResponsePDU obj = new CmsSelectActiveSGResponsePDU();
        byte[] data = obj.encode("uper");
        CmsSelectActiveSGResponsePDU d = CmsSelectActiveSGResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
