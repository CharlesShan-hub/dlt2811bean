// Auto-generated. Tests for CmsSetFileRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetFileRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetFileRequestPDU obj = new CmsSetFileRequestPDU();
        assertNull(obj.filename);
        assertEquals(0, obj.start_position);
        assertNull(obj.file_data);
        assertFalse(obj.end_of_file);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetFileRequestPDU obj = new CmsSetFileRequestPDU();
        obj.filename = "test";
        obj.start_position = 42;
        obj.file_data = new byte[0];
        String json = MAPPER.writeValueAsString(obj);
        CmsSetFileRequestPDU d = MAPPER.readValue(json, CmsSetFileRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetFileRequestPDU obj = new CmsSetFileRequestPDU();
        obj.filename = "test";
        obj.start_position = 42;
        obj.file_data = new byte[0];
        byte[] data = obj.encode("uper");
        CmsSetFileRequestPDU d = CmsSetFileRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
