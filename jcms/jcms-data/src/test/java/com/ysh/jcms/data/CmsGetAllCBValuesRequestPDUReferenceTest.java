// Auto-generated. Tests for CmsGetAllCBValuesRequestPDUReference

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetAllCBValuesRequestPDUReferenceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoiceldName() throws Exception {
        CmsGetAllCBValuesRequestPDUReference obj = new CmsGetAllCBValuesRequestPDUReference();
        obj._choice = "ldName";
        obj.ldName = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllCBValuesRequestPDUReference d = MAPPER.readValue(json, CmsGetAllCBValuesRequestPDUReference.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoicelnReference() throws Exception {
        CmsGetAllCBValuesRequestPDUReference obj = new CmsGetAllCBValuesRequestPDUReference();
        obj._choice = "lnReference";
        obj.lnReference = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllCBValuesRequestPDUReference d = MAPPER.readValue(json, CmsGetAllCBValuesRequestPDUReference.class);
        assertEquals(obj, d);
    }

    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetAllCBValuesRequestPDUReference obj = new CmsGetAllCBValuesRequestPDUReference();
        obj._choice = "ldName";
        obj.ldName = "test";
        byte[] data = obj.encode("uper");
        CmsGetAllCBValuesRequestPDUReference d = CmsGetAllCBValuesRequestPDUReference.decode("uper", data);
        assertEquals(obj, d);
    }
}
