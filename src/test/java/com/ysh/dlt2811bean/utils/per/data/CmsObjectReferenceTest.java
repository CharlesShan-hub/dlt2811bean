package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsObjectReferenceTest {

    @Test
    void encodeDecode_basic() throws Exception {
        String ref = "S1LD1/LLN0.Health";

        PerOutputStream pos = new PerOutputStream();
        CmsObjectReference.encode(pos, ref);
        byte[] data = pos.toByteArray();

        PerInputStream pis = new PerInputStream(data);
        String result = CmsObjectReference.decode(pis);

        assertEquals(ref, result);
    }

    @Test
    void encodeDecode_empty() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsObjectReference.encode(pos, "");
        byte[] data = pos.toByteArray();

        PerInputStream pis = new PerInputStream(data);
        assertEquals("", CmsObjectReference.decode(pis));
    }

    @Test
    void encodeDecode_nullBecomesEmpty() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsObjectReference.encode(pos, null);
        byte[] data = pos.toByteArray();

        PerInputStream pis = new PerInputStream(data);
        assertEquals("", CmsObjectReference.decode(pis));
    }

    @Test
    void encodeDecode_maxLength() throws Exception {
        // 129 chars
        String ref = "LD" + "/LN" + "." + "A".repeat(121);

        PerOutputStream pos = new PerOutputStream();
        CmsObjectReference.encode(pos, ref);
        byte[] data = pos.toByteArray();

        PerInputStream pis = new PerInputStream(data);
        assertEquals(ref, CmsObjectReference.decode(pis));
    }

    @Test
    void encode_rejectsTooLong() {
        // 130 chars: "LD/LN." (6) + 124 A's
        String ref = "LD" + "/LN" + "." + "A".repeat(124);
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsObjectReference.encode(pos, ref));
    }

    @Test
    void maxLength_is129() {
        assertEquals(129, CmsObjectReference.MAX_LENGTH);
    }
}
