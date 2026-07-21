// Auto-generated. Tests for CmsAnonymousGetLogStatusValuesResponsePDULog

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetLogStatusValuesResponsePDULogTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoiceerror() throws Exception {
        CmsAnonymousGetLogStatusValuesResponsePDULog obj = new CmsAnonymousGetLogStatusValuesResponsePDULog();
        obj._choice = "error";
        obj.error = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetLogStatusValuesResponsePDULog d = MAPPER.readValue(json, CmsAnonymousGetLogStatusValuesResponsePDULog.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoicevalue() throws Exception {
        CmsAnonymousGetLogStatusValuesResponsePDULog obj = new CmsAnonymousGetLogStatusValuesResponsePDULog();
        obj._choice = "value";
        obj.value = new CmsAnonymousGetLogStatusValuesResponsePDULogValue();
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetLogStatusValuesResponsePDULog d = MAPPER.readValue(json, CmsAnonymousGetLogStatusValuesResponsePDULog.class);
        assertEquals(obj, d);
    }

}
