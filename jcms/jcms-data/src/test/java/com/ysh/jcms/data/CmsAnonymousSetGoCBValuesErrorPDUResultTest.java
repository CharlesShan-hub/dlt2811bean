// Auto-generated. Tests for CmsAnonymousSetGoCBValuesErrorPDUResult

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousSetGoCBValuesErrorPDUResultTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousSetGoCBValuesErrorPDUResult obj = new CmsAnonymousSetGoCBValuesErrorPDUResult();
        assertNull(obj.error);
        assertNull(obj.go_ena);
        assertNull(obj.go_id);
        assertNull(obj.dat_set);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousSetGoCBValuesErrorPDUResult obj = new CmsAnonymousSetGoCBValuesErrorPDUResult();
        obj.error = 1;
        obj.go_ena = 1;
        obj.go_id = 1;
        obj.dat_set = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetGoCBValuesErrorPDUResult d = MAPPER.readValue(json, CmsAnonymousSetGoCBValuesErrorPDUResult.class);
        assertEquals(obj, d);
    }
}
