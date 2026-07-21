// Auto-generated. Tests for CmsAnonymousGetSGCBValuesResponsePDUSgscb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetSGCBValuesResponsePDUSgscbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoiceerror() throws Exception {
        CmsAnonymousGetSGCBValuesResponsePDUSgscb obj = new CmsAnonymousGetSGCBValuesResponsePDUSgscb();
        obj._choice = "error";
        obj.error = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetSGCBValuesResponsePDUSgscb d = MAPPER.readValue(json, CmsAnonymousGetSGCBValuesResponsePDUSgscb.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoicevalue() throws Exception {
        CmsAnonymousGetSGCBValuesResponsePDUSgscb obj = new CmsAnonymousGetSGCBValuesResponsePDUSgscb();
        obj._choice = "value";
        obj.value = new CmsSGCB();
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetSGCBValuesResponsePDUSgscb d = MAPPER.readValue(json, CmsAnonymousGetSGCBValuesResponsePDUSgscb.class);
        assertEquals(obj, d);
    }

    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousGetSGCBValuesResponsePDUSgscb obj = new CmsAnonymousGetSGCBValuesResponsePDUSgscb();
        obj._choice = "error";
        obj.error = 42;
        byte[] data = obj.encode("uper");
        CmsAnonymousGetSGCBValuesResponsePDUSgscb d = CmsAnonymousGetSGCBValuesResponsePDUSgscb.decode("uper", data);
        assertEquals(obj, d);
    }
}
