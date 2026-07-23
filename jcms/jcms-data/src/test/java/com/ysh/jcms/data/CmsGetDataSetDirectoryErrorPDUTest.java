// Auto-generated. Tests for CmsGetDataSetDirectoryErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataSetDirectoryErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataSetDirectoryErrorPDU obj = new CmsGetDataSetDirectoryErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetDataSetDirectoryErrorPDU obj = new CmsGetDataSetDirectoryErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataSetDirectoryErrorPDU obj = new CmsGetDataSetDirectoryErrorPDU(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataSetDirectoryErrorPDU d = MAPPER.readValue(json, CmsGetDataSetDirectoryErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetDataSetDirectoryErrorPDU obj = new CmsGetDataSetDirectoryErrorPDU(1);
        byte[] data = obj.encode("uper");
        CmsGetDataSetDirectoryErrorPDU d = CmsGetDataSetDirectoryErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
