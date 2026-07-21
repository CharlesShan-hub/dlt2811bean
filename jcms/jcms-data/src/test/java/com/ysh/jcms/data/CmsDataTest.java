// Auto-generated. Tests for CmsData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoiceerror() throws Exception {
        CmsData obj = new CmsData();
        obj._choice = "error";
        obj.error = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsData d = MAPPER.readValue(json, CmsData.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoicearray() throws Exception {
        CmsData obj = new CmsData();
        obj._choice = "array";
        String json = MAPPER.writeValueAsString(obj);
        CmsData d = MAPPER.readValue(json, CmsData.class);
        assertEquals(obj, d);
    }

    @Test
    public void testEncodeDecode() throws Exception {
        CmsData obj = new CmsData();
        obj._choice = "error";
        obj.error = 42;
        byte[] data = obj.encode("uper");
        CmsData d = CmsData.decode("uper", data);
        assertEquals(obj, d);
    }
}
