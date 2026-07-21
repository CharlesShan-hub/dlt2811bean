// Auto-generated. Tests for CmsQueryLogAfterErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsQueryLogAfterErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsQueryLogAfterErrorPDU obj = new CmsQueryLogAfterErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsQueryLogAfterErrorPDU obj = new CmsQueryLogAfterErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsQueryLogAfterErrorPDU obj = new CmsQueryLogAfterErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsQueryLogAfterErrorPDU d = MAPPER.readValue(json, CmsQueryLogAfterErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsQueryLogAfterErrorPDU obj = new CmsQueryLogAfterErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsQueryLogAfterErrorPDU d = CmsQueryLogAfterErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
