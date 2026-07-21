// Auto-generated. Tests for CmsGetLogicalNodeDirectoryRequestPDUReference

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetLogicalNodeDirectoryRequestPDUReferenceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoiceldName() throws Exception {
        CmsGetLogicalNodeDirectoryRequestPDUReference obj = new CmsGetLogicalNodeDirectoryRequestPDUReference();
        obj._choice = "ldName";
        obj.ldName = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLogicalNodeDirectoryRequestPDUReference d = MAPPER.readValue(json, CmsGetLogicalNodeDirectoryRequestPDUReference.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoicelnReference() throws Exception {
        CmsGetLogicalNodeDirectoryRequestPDUReference obj = new CmsGetLogicalNodeDirectoryRequestPDUReference();
        obj._choice = "lnReference";
        obj.lnReference = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLogicalNodeDirectoryRequestPDUReference d = MAPPER.readValue(json, CmsGetLogicalNodeDirectoryRequestPDUReference.class);
        assertEquals(obj, d);
    }

    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetLogicalNodeDirectoryRequestPDUReference obj = new CmsGetLogicalNodeDirectoryRequestPDUReference();
        obj._choice = "ldName";
        obj.ldName = "test";
        byte[] data = obj.encode("uper");
        CmsGetLogicalNodeDirectoryRequestPDUReference d = CmsGetLogicalNodeDirectoryRequestPDUReference.decode("uper", data);
        assertEquals(obj, d);
    }
}
