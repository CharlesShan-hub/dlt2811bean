// Auto-generated. Tests for CmsAnonymousSetGoCBValuesRequestPDUGocb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousSetGoCBValuesRequestPDUGocbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousSetGoCBValuesRequestPDUGocb obj = new CmsAnonymousSetGoCBValuesRequestPDUGocb();
        assertNull(obj.reference);
        assertNull(obj.go_ena);
        assertNull(obj.go_id);
        assertNull(obj.dat_set);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousSetGoCBValuesRequestPDUGocb obj = new CmsAnonymousSetGoCBValuesRequestPDUGocb();
        obj.reference = "test";
        obj.go_ena = true;
        obj.go_id = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetGoCBValuesRequestPDUGocb d = MAPPER.readValue(json, CmsAnonymousSetGoCBValuesRequestPDUGocb.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousSetGoCBValuesRequestPDUGocb obj = new CmsAnonymousSetGoCBValuesRequestPDUGocb();
        obj.reference = "test";
        obj.go_ena = true;
        obj.go_id = "test";
        byte[] data = obj.encode("uper");
        CmsAnonymousSetGoCBValuesRequestPDUGocb d = CmsAnonymousSetGoCBValuesRequestPDUGocb.decode("uper", data);
        assertEquals(obj, d);
    }
}
