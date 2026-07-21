// Auto-generated. Tests for CmsAnonymousGetBRCBValuesResponsePDUBrcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetBRCBValuesResponsePDUBrcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoiceerror() throws Exception {
        CmsAnonymousGetBRCBValuesResponsePDUBrcb obj = new CmsAnonymousGetBRCBValuesResponsePDUBrcb();
        obj._choice = "error";
        obj.error = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetBRCBValuesResponsePDUBrcb d = MAPPER.readValue(json, CmsAnonymousGetBRCBValuesResponsePDUBrcb.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoicevalue() throws Exception {
        CmsAnonymousGetBRCBValuesResponsePDUBrcb obj = new CmsAnonymousGetBRCBValuesResponsePDUBrcb();
        obj._choice = "value";
        obj.value = new CmsBRCB();
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetBRCBValuesResponsePDUBrcb d = MAPPER.readValue(json, CmsAnonymousGetBRCBValuesResponsePDUBrcb.class);
        assertEquals(obj, d);
    }

    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousGetBRCBValuesResponsePDUBrcb obj = new CmsAnonymousGetBRCBValuesResponsePDUBrcb();
        obj._choice = "error";
        obj.error = 42;
        byte[] data = obj.encode("uper");
        CmsAnonymousGetBRCBValuesResponsePDUBrcb d = CmsAnonymousGetBRCBValuesResponsePDUBrcb.decode("uper", data);
        assertEquals(obj, d);
    }
}
