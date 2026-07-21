// Auto-generated. Tests for CmsAnonymousLogEntryEntryData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousLogEntryEntryDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousLogEntryEntryData obj = new CmsAnonymousLogEntryEntryData();
        assertNull(obj.reference);
        assertNull(obj.fc);
        assertNull(obj.value);
        assertEquals(0, obj.reason);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousLogEntryEntryData obj = new CmsAnonymousLogEntryEntryData();
        obj.reference = "test";
        obj.fc = "test";
        if (obj.value == null) obj.value = new CmsData();
        obj.reason = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousLogEntryEntryData d = MAPPER.readValue(json, CmsAnonymousLogEntryEntryData.class);
        assertEquals(obj, d);
    }
}
