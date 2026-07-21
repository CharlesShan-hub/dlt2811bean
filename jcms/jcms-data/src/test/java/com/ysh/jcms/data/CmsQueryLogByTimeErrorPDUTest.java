// Auto-generated. Tests for CmsQueryLogByTimeErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsQueryLogByTimeErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsQueryLogByTimeErrorPDU obj = new CmsQueryLogByTimeErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsQueryLogByTimeErrorPDU obj = new CmsQueryLogByTimeErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsQueryLogByTimeErrorPDU obj = new CmsQueryLogByTimeErrorPDU(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsQueryLogByTimeErrorPDU d = MAPPER.readValue(json, CmsQueryLogByTimeErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsQueryLogByTimeErrorPDU obj = new CmsQueryLogByTimeErrorPDU(1);
        byte[] data = obj.encode("uper");
        CmsQueryLogByTimeErrorPDU d = CmsQueryLogByTimeErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
