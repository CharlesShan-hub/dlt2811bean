// Auto-generated. Tests for CmsConfirmEditSGValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsConfirmEditSGValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsConfirmEditSGValuesResponsePDU obj = new CmsConfirmEditSGValuesResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsConfirmEditSGValuesResponsePDU obj = new CmsConfirmEditSGValuesResponsePDU(null);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsConfirmEditSGValuesResponsePDU obj = new CmsConfirmEditSGValuesResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsConfirmEditSGValuesResponsePDU d = MAPPER.readValue(json, CmsConfirmEditSGValuesResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsConfirmEditSGValuesResponsePDU obj = new CmsConfirmEditSGValuesResponsePDU();
        byte[] data = obj.encode("uper");
        CmsConfirmEditSGValuesResponsePDU d = CmsConfirmEditSGValuesResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
