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
        obj.error = 42;
        obj.go_ena = 42;
        obj.go_id = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetGoCBValuesErrorPDUResult d = MAPPER.readValue(json, CmsAnonymousSetGoCBValuesErrorPDUResult.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousSetGoCBValuesErrorPDUResult obj = new CmsAnonymousSetGoCBValuesErrorPDUResult();
        obj.error = 42;
        obj.go_ena = 42;
        obj.go_id = 42;
        byte[] data = obj.encode("uper");
        CmsAnonymousSetGoCBValuesErrorPDUResult d = CmsAnonymousSetGoCBValuesErrorPDUResult.decode("uper", data);
        assertEquals(obj, d);
    }
}
