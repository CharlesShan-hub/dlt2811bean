// Auto-generated. Tests for CmsFileEntry

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsFileEntryTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsFileEntry obj = new CmsFileEntry();
        assertNull(obj.file_name);
        assertEquals(0, obj.file_size);
        assertNull(obj.last_modified);
        assertEquals(0, obj.check_sum);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsFileEntry obj = new CmsFileEntry();
        obj.file_name = "test";
        obj.file_size = 1;
        obj.last_modified = new byte[]{0x01, 0x02};
        obj.check_sum = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsFileEntry d = MAPPER.readValue(json, CmsFileEntry.class);
        assertEquals(obj, d);
    }
}
