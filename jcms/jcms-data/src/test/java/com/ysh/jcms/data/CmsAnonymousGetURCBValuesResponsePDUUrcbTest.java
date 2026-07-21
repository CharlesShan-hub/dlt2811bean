// Auto-generated. Tests for CmsAnonymousGetURCBValuesResponsePDUUrcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetURCBValuesResponsePDUUrcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoiceerror() throws Exception {
        CmsAnonymousGetURCBValuesResponsePDUUrcb obj = new CmsAnonymousGetURCBValuesResponsePDUUrcb();
        obj._choice = "error";
        obj.error = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetURCBValuesResponsePDUUrcb d = MAPPER.readValue(json, CmsAnonymousGetURCBValuesResponsePDUUrcb.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoicevalue() throws Exception {
        CmsAnonymousGetURCBValuesResponsePDUUrcb obj = new CmsAnonymousGetURCBValuesResponsePDUUrcb();
        obj._choice = "value";
        obj.value = new CmsURCB();
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetURCBValuesResponsePDUUrcb d = MAPPER.readValue(json, CmsAnonymousGetURCBValuesResponsePDUUrcb.class);
        assertEquals(obj, d);
    }

    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousGetURCBValuesResponsePDUUrcb obj = new CmsAnonymousGetURCBValuesResponsePDUUrcb();
        obj._choice = "error";
        obj.error = 42;
        byte[] data = obj.encode("uper");
        CmsAnonymousGetURCBValuesResponsePDUUrcb d = CmsAnonymousGetURCBValuesResponsePDUUrcb.decode("uper", data);
        assertEquals(obj, d);
    }
}
