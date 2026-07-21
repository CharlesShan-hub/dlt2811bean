// Auto-generated. Tests for CmsSetGoCBValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetGoCBValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetGoCBValuesRequestPDU obj = new CmsSetGoCBValuesRequestPDU();
        assertNull(obj.gocb);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetGoCBValuesRequestPDU obj = new CmsSetGoCBValuesRequestPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetGoCBValuesRequestPDU d = MAPPER.readValue(json, CmsSetGoCBValuesRequestPDU.class);
        assertEquals(obj, d);
    }
}
