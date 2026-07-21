// Auto-generated. Tests for CmsSetEditSGValueResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetEditSGValueResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetEditSGValueResponsePDU obj = new CmsSetEditSGValueResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSetEditSGValueResponsePDU obj = new CmsSetEditSGValueResponsePDU(null);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetEditSGValueResponsePDU obj = new CmsSetEditSGValueResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetEditSGValueResponsePDU d = MAPPER.readValue(json, CmsSetEditSGValueResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetEditSGValueResponsePDU obj = new CmsSetEditSGValueResponsePDU();
        byte[] data = obj.encode("uper");
        CmsSetEditSGValueResponsePDU d = CmsSetEditSGValueResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
