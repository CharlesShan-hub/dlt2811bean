// Auto-generated. Tests for CmsAnonymousReportPDUEntryEntryData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousReportPDUEntryEntryDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousReportPDUEntryEntryData obj = new CmsAnonymousReportPDUEntryEntryData();
        assertNull(obj.reference);
        assertNull(obj.fc);
        assertEquals(0, obj.id);
        assertNull(obj.value);
        assertNull(obj.reason);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousReportPDUEntryEntryData obj = new CmsAnonymousReportPDUEntryEntryData();
        obj.reference = "test";
        obj.fc = "test";
        obj.id = 1;
        if (obj.value == null) obj.value = new CmsData();
        obj.reason = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousReportPDUEntryEntryData d = MAPPER.readValue(json, CmsAnonymousReportPDUEntryEntryData.class);
        assertEquals(obj, d);
    }
}
