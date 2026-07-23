// Auto-generated. Tests for CmsAnonymousGetAllCBValuesResponsePDUCbValue

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetAllCBValuesResponsePDUCbValueTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousGetAllCBValuesResponsePDUCbValue obj = new CmsAnonymousGetAllCBValuesResponsePDUCbValue();
        assertNull(obj.reference);
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousGetAllCBValuesResponsePDUCbValue obj = new CmsAnonymousGetAllCBValuesResponsePDUCbValue();
        obj.reference = "test";
        if (obj.value == null) obj.value = new CmsAnonymousGetAllCBValuesResponsePDUCbValueValue();
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetAllCBValuesResponsePDUCbValue d = MAPPER.readValue(json, CmsAnonymousGetAllCBValuesResponsePDUCbValue.class);
        assertEquals(obj, d);
    }
}
