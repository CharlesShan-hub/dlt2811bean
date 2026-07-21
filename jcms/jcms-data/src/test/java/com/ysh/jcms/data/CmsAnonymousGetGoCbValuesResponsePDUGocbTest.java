// Auto-generated. Tests for CmsAnonymousGetGoCbValuesResponsePDUGocb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetGoCbValuesResponsePDUGocbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoiceerror() throws Exception {
        CmsAnonymousGetGoCbValuesResponsePDUGocb obj = new CmsAnonymousGetGoCbValuesResponsePDUGocb();
        obj._choice = "error";
        obj.error = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetGoCbValuesResponsePDUGocb d = MAPPER.readValue(json, CmsAnonymousGetGoCbValuesResponsePDUGocb.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoicevalue() throws Exception {
        CmsAnonymousGetGoCbValuesResponsePDUGocb obj = new CmsAnonymousGetGoCbValuesResponsePDUGocb();
        obj._choice = "value";
        obj.value = new CmsGoCB();
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetGoCbValuesResponsePDUGocb d = MAPPER.readValue(json, CmsAnonymousGetGoCbValuesResponsePDUGocb.class);
        assertEquals(obj, d);
    }

    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousGetGoCbValuesResponsePDUGocb obj = new CmsAnonymousGetGoCbValuesResponsePDUGocb();
        obj._choice = "error";
        obj.error = 42;
        byte[] data = obj.encode("uper");
        CmsAnonymousGetGoCbValuesResponsePDUGocb d = CmsAnonymousGetGoCbValuesResponsePDUGocb.decode("uper", data);
        assertEquals(obj, d);
    }
}
