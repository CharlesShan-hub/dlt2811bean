// Auto-generated. Tests for CmsReportPDUEntryEntryData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsReportPDUEntryEntryDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsReportPDUEntryEntryData obj = new CmsReportPDUEntryEntryData();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsReportPDUEntryEntryData obj = new CmsReportPDUEntryEntryData();
        String json = MAPPER.writeValueAsString(obj);
        CmsReportPDUEntryEntryData d = MAPPER.readValue(json, CmsReportPDUEntryEntryData.class);
        assertEquals(obj, d);
    }
}
