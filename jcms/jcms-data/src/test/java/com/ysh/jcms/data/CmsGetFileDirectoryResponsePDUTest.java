// Auto-generated. Tests for CmsGetFileDirectoryResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetFileDirectoryResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetFileDirectoryResponsePDU obj = new CmsGetFileDirectoryResponsePDU();
        assertNotNull(obj.file_entry);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetFileDirectoryResponsePDU obj = new CmsGetFileDirectoryResponsePDU();
        obj.file_entry = java.util.Collections.singletonList(new CmsFileEntry());
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetFileDirectoryResponsePDU d = MAPPER.readValue(json, CmsGetFileDirectoryResponsePDU.class);
        assertEquals(obj, d);
    }
}
