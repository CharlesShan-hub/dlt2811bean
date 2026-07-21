// Auto-generated. Tests for CmsSetEditSGValueErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetEditSGValueErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetEditSGValueErrorPDU obj = new CmsSetEditSGValueErrorPDU();
        assertNotNull(obj.result);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetEditSGValueErrorPDU obj = new CmsSetEditSGValueErrorPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetEditSGValueErrorPDU d = MAPPER.readValue(json, CmsSetEditSGValueErrorPDU.class);
        assertEquals(obj, d);
    }
}
