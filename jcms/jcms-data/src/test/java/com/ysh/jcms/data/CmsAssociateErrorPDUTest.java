// Auto-generated. Tests for CmsAssociateErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAssociateErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAssociateErrorPDU obj = new CmsAssociateErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsAssociateErrorPDU obj = new CmsAssociateErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAssociateErrorPDU obj = new CmsAssociateErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsAssociateErrorPDU d = MAPPER.readValue(json, CmsAssociateErrorPDU.class);
        assertEquals(obj, d);
    }
}
