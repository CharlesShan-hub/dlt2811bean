// Auto-generated. Tests for CmsReleaseErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsReleaseErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsReleaseErrorPDU obj = new CmsReleaseErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsReleaseErrorPDU obj = new CmsReleaseErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsReleaseErrorPDU obj = new CmsReleaseErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsReleaseErrorPDU d = MAPPER.readValue(json, CmsReleaseErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsReleaseErrorPDU obj = new CmsReleaseErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsReleaseErrorPDU d = CmsReleaseErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
