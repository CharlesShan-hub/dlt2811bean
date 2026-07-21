// Auto-generated. Tests for CmsSetEditSGValueRequestPDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetEditSGValueRequestPDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetEditSGValueRequestPDUData obj = new CmsSetEditSGValueRequestPDUData();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetEditSGValueRequestPDUData obj = new CmsSetEditSGValueRequestPDUData();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetEditSGValueRequestPDUData d = MAPPER.readValue(json, CmsSetEditSGValueRequestPDUData.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetEditSGValueRequestPDUData obj = new CmsSetEditSGValueRequestPDUData();
        byte[] data = obj.encode("uper");
        CmsSetEditSGValueRequestPDUData d = CmsSetEditSGValueRequestPDUData.decode("uper", data);
        assertEquals(obj, d);
    }
}
