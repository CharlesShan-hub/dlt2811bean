// Auto-generated. Tests for CmsSetGoCBValuesRequestPDUGocb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetGoCBValuesRequestPDUGocbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetGoCBValuesRequestPDUGocb obj = new CmsSetGoCBValuesRequestPDUGocb();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetGoCBValuesRequestPDUGocb obj = new CmsSetGoCBValuesRequestPDUGocb();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetGoCBValuesRequestPDUGocb d = MAPPER.readValue(json, CmsSetGoCBValuesRequestPDUGocb.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetGoCBValuesRequestPDUGocb obj = new CmsSetGoCBValuesRequestPDUGocb();
        byte[] data = obj.encode("uper");
        CmsSetGoCBValuesRequestPDUGocb d = CmsSetGoCBValuesRequestPDUGocb.decode("uper", data);
        assertEquals(obj, d);
    }
}
