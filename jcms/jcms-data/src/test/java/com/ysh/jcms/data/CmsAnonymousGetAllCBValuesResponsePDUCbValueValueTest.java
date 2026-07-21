// Auto-generated. Tests for CmsAnonymousGetAllCBValuesResponsePDUCbValueValue

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetAllCBValuesResponsePDUCbValueValueTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoicebrcb() throws Exception {
        CmsAnonymousGetAllCBValuesResponsePDUCbValueValue obj = new CmsAnonymousGetAllCBValuesResponsePDUCbValueValue();
        obj._choice = "brcb";
        obj.brcb = new CmsBRCB();
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetAllCBValuesResponsePDUCbValueValue d = MAPPER.readValue(json, CmsAnonymousGetAllCBValuesResponsePDUCbValueValue.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoiceurcb() throws Exception {
        CmsAnonymousGetAllCBValuesResponsePDUCbValueValue obj = new CmsAnonymousGetAllCBValuesResponsePDUCbValueValue();
        obj._choice = "urcb";
        obj.urcb = new CmsURCB();
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetAllCBValuesResponsePDUCbValueValue d = MAPPER.readValue(json, CmsAnonymousGetAllCBValuesResponsePDUCbValueValue.class);
        assertEquals(obj, d);
    }

}
