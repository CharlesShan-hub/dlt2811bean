// Auto-generated. Tests for CmsSmpMod

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSmpModTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSmpMod obj = new CmsSmpMod();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSmpMod obj = new CmsSmpMod(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSmpMod obj = new CmsSmpMod(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsSmpMod d = MAPPER.readValue(json, CmsSmpMod.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSmpMod obj = new CmsSmpMod(42);
        byte[] data = obj.encode("uper");
        CmsSmpMod d = CmsSmpMod.decode("uper", data);
        assertEquals(obj, d);
    }
}
