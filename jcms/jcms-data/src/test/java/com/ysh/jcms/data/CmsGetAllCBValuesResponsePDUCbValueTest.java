// Auto-generated. Tests for CmsGetAllCBValuesResponsePDUCbValue

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetAllCBValuesResponsePDUCbValueTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetAllCBValuesResponsePDUCbValue obj = new CmsGetAllCBValuesResponsePDUCbValue();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetAllCBValuesResponsePDUCbValue obj = new CmsGetAllCBValuesResponsePDUCbValue();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllCBValuesResponsePDUCbValue d = MAPPER.readValue(json, CmsGetAllCBValuesResponsePDUCbValue.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetAllCBValuesResponsePDUCbValue obj = new CmsGetAllCBValuesResponsePDUCbValue();
        byte[] data = obj.encode("uper");
        CmsGetAllCBValuesResponsePDUCbValue d = CmsGetAllCBValuesResponsePDUCbValue.decode("uper", data);
        assertEquals(obj, d);
    }
}
