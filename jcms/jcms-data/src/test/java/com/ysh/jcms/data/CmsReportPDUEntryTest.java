// Auto-generated. Tests for CmsReportPDUEntry

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsReportPDUEntryTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsReportPDUEntry obj = new CmsReportPDUEntry();
        assertNull(obj.time_of_entry);
        assertNull(obj.entry_id);
        assertNull(obj.entry_data);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsReportPDUEntry obj = new CmsReportPDUEntry();
        obj.time_of_entry = new byte[0];
        obj.entry_id = new byte[0];
        String json = MAPPER.writeValueAsString(obj);
        CmsReportPDUEntry d = MAPPER.readValue(json, CmsReportPDUEntry.class);
        assertEquals(obj, d);
    }
}
