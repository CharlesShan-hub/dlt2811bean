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
        CmsGetDataSetDirectoryErrorPDU obj = new CmsGetDataSetDirectoryErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataSetDirectoryErrorPDU d = MAPPER.readValue(json, CmsGetDataSetDirectoryErrorPDU.class);
        assertEquals(obj, d);
    }
}
