// Auto-generated. Tests for CmsTcmd

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsTcmdTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsTcmd obj = new CmsTcmd();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsTcmd obj = new CmsTcmd(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsTcmd obj = new CmsTcmd(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsTcmd d = MAPPER.readValue(json, CmsTcmd.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsTcmd obj = new CmsTcmd(42);
        byte[] data = obj.encode("uper");
        CmsTcmd d = CmsTcmd.decode("uper", data);
        assertEquals(obj, d);
    }
}
