// Auto-generated. Tests for CmsSetEditSGValueRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetEditSGValueRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetEditSGValueRequestPDU obj = new CmsSetEditSGValueRequestPDU();
        assertNull(obj.data);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetEditSGValueRequestPDU obj = new CmsSetEditSGValueRequestPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetEditSGValueRequestPDU d = MAPPER.readValue(json, CmsSetEditSGValueRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetEditSGValueRequestPDU obj = new CmsSetEditSGValueRequestPDU();
        byte[] data = obj.encode("uper");
        CmsSetEditSGValueRequestPDU d = CmsSetEditSGValueRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
