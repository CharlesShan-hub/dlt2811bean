// Auto-generated. Tests for CmsAnonymousGetMSVCBValuesResponsePDUMsvcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetMSVCBValuesResponsePDUMsvcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoiceerror() throws Exception {
        CmsAnonymousGetMSVCBValuesResponsePDUMsvcb obj = new CmsAnonymousGetMSVCBValuesResponsePDUMsvcb();
        obj._choice = "error";
        obj.error = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetMSVCBValuesResponsePDUMsvcb d = MAPPER.readValue(json, CmsAnonymousGetMSVCBValuesResponsePDUMsvcb.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoicevalue() throws Exception {
        CmsAnonymousGetMSVCBValuesResponsePDUMsvcb obj = new CmsAnonymousGetMSVCBValuesResponsePDUMsvcb();
        obj._choice = "value";
        obj.value = new CmsMSVCB();
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetMSVCBValuesResponsePDUMsvcb d = MAPPER.readValue(json, CmsAnonymousGetMSVCBValuesResponsePDUMsvcb.class);
        assertEquals(obj, d);
    }

    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousGetMSVCBValuesResponsePDUMsvcb obj = new CmsAnonymousGetMSVCBValuesResponsePDUMsvcb();
        obj._choice = "error";
        obj.error = 42;
        byte[] data = obj.encode("uper");
        CmsAnonymousGetMSVCBValuesResponsePDUMsvcb d = CmsAnonymousGetMSVCBValuesResponsePDUMsvcb.decode("uper", data);
        assertEquals(obj, d);
    }
}
