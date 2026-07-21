// Auto-generated. Tests for CmsLogEntry

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsLogEntryTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsLogEntry obj = new CmsLogEntry();
        assertNull(obj.time_of_entry);
        assertNull(obj.entry_id);
        assertNull(obj.entry_data);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsLogEntry obj = new CmsLogEntry();
        obj.time_of_entry = new byte[0];
        obj.entry_id = new byte[0];
        String json = MAPPER.writeValueAsString(obj);
        CmsLogEntry d = MAPPER.readValue(json, CmsLogEntry.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsLogEntry obj = new CmsLogEntry();
        obj.time_of_entry = new byte[0];
        obj.entry_id = new byte[0];
        byte[] data = obj.encode("uper");
        CmsLogEntry d = CmsLogEntry.decode("uper", data);
        assertEquals(obj, d);
    }
}
