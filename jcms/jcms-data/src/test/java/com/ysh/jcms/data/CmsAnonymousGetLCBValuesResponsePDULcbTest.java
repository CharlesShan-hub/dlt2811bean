// Auto-generated. Tests for CmsAnonymousGetLCBValuesResponsePDULcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetLCBValuesResponsePDULcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoiceerror() throws Exception {
        CmsAnonymousGetLCBValuesResponsePDULcb obj = new CmsAnonymousGetLCBValuesResponsePDULcb();
        obj._choice = "error";
        obj.error = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetLCBValuesResponsePDULcb d = MAPPER.readValue(json, CmsAnonymousGetLCBValuesResponsePDULcb.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoicevalue() throws Exception {
        CmsAnonymousGetLCBValuesResponsePDULcb obj = new CmsAnonymousGetLCBValuesResponsePDULcb();
        obj._choice = "value";
        obj.value = new CmsLCB();
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetLCBValuesResponsePDULcb d = MAPPER.readValue(json, CmsAnonymousGetLCBValuesResponsePDULcb.class);
        assertEquals(obj, d);
    }

}
