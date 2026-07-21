// Auto-generated. Tests for CmsGetEditSGValueRequestPDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetEditSGValueRequestPDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetEditSGValueRequestPDUData obj = new CmsGetEditSGValueRequestPDUData();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetEditSGValueRequestPDUData obj = new CmsGetEditSGValueRequestPDUData();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetEditSGValueRequestPDUData d = MAPPER.readValue(json, CmsGetEditSGValueRequestPDUData.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetEditSGValueRequestPDUData obj = new CmsGetEditSGValueRequestPDUData();
        byte[] data = obj.encode("uper");
        CmsGetEditSGValueRequestPDUData d = CmsGetEditSGValueRequestPDUData.decode("uper", data);
        assertEquals(obj, d);
    }
}
