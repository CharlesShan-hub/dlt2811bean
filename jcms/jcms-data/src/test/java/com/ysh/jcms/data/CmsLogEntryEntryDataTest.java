// Auto-generated. Tests for CmsLogEntryEntryData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsLogEntryEntryDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsLogEntryEntryData obj = new CmsLogEntryEntryData();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsLogEntryEntryData obj = new CmsLogEntryEntryData();
        String json = MAPPER.writeValueAsString(obj);
        CmsLogEntryEntryData d = MAPPER.readValue(json, CmsLogEntryEntryData.class);
        assertEquals(obj, d);
    }
}
