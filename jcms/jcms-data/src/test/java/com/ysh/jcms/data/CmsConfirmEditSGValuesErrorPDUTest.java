// Auto-generated. Tests for CmsConfirmEditSGValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsConfirmEditSGValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsConfirmEditSGValuesErrorPDU obj = new CmsConfirmEditSGValuesErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsConfirmEditSGValuesErrorPDU obj = new CmsConfirmEditSGValuesErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsConfirmEditSGValuesErrorPDU obj = new CmsConfirmEditSGValuesErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsConfirmEditSGValuesErrorPDU d = MAPPER.readValue(json, CmsConfirmEditSGValuesErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsConfirmEditSGValuesErrorPDU obj = new CmsConfirmEditSGValuesErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsConfirmEditSGValuesErrorPDU d = CmsConfirmEditSGValuesErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
