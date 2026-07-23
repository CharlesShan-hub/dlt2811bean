// Auto-generated. Tests for CmsGetFileDirectoryRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetFileDirectoryRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetFileDirectoryRequestPDU obj = new CmsGetFileDirectoryRequestPDU();
        assertNull(obj.path_name);
        assertNull(obj.start_time);
        assertNull(obj.stop_time);
        assertNull(obj.file_after);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetFileDirectoryRequestPDU obj = new CmsGetFileDirectoryRequestPDU();
        obj.path_name = "test";
        obj.start_time = new byte[]{0x01, 0x02};
        obj.stop_time = new byte[]{0x01, 0x02};
        obj.file_after = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetFileDirectoryRequestPDU d = MAPPER.readValue(json, CmsGetFileDirectoryRequestPDU.class);
        assertEquals(obj, d);
    }
}
