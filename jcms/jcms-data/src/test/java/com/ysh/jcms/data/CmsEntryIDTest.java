// Auto-generated. Tests for CmsEntryID

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsEntryIDTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsEntryID obj = new CmsEntryID();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsEntryID obj = new CmsEntryID();
        obj.value = new byte[]{0x01, 0x02};
        String json = MAPPER.writeValueAsString(obj);
        CmsEntryID d = MAPPER.readValue(json, CmsEntryID.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsEntryID obj = new CmsEntryID();
        obj.value = new byte[]{0x01, 0x02};
        byte[] data = obj.encode("uper");
        CmsEntryID d = CmsEntryID.decode("uper", data);
        assertEquals(obj, d);
    }
}
