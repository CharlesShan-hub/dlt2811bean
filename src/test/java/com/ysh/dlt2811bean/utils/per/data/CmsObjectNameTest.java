package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsObjectNameTest {

    @Test
    void default_isEmpty() {
        CmsObjectName name = new CmsObjectName();
        assertEquals("", name.getValue());
    }

    @Test
    void construct_normal() {
        CmsObjectName name = new CmsObjectName("LD1");
        assertEquals("LD1", name.getValue());
    }

    @Test
    void construct_maxLength() {
        String s = "A".repeat(64);
        CmsObjectName name = new CmsObjectName(s);
        assertEquals(64, name.getValue().length());
    }

    @Test
    void construct_exceedsMaxLength_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new CmsObjectName("A".repeat(65)));
    }

    @Test
    void construct_null_treatedAsEmpty() {
        CmsObjectName name = new CmsObjectName(null);
        assertEquals("", name.getValue());
    }

    @Test
    void encodeDecode_normal() throws Exception {
        CmsObjectName name = new CmsObjectName("LD1");
        PerOutputStream pos = new PerOutputStream();
        CmsObjectName.encode(pos, name);
        CmsObjectName r = CmsObjectName.decode(new PerInputStream(pos.toByteArray()));
        assertEquals("LD1", r.getValue());
    }

    @Test
    void encodeDecode_empty() throws Exception {
        CmsObjectName name = new CmsObjectName();
        PerOutputStream pos = new PerOutputStream();
        CmsObjectName.encode(pos, name);
        CmsObjectName r = CmsObjectName.decode(new PerInputStream(pos.toByteArray()));
        assertEquals("", r.getValue());
    }

    @Test
    void encodeDecode_maxLength() throws Exception {
        String s = "X".repeat(64);
        CmsObjectName name = new CmsObjectName(s);
        PerOutputStream pos = new PerOutputStream();
        CmsObjectName.encode(pos, name);
        CmsObjectName r = CmsObjectName.decode(new PerInputStream(pos.toByteArray()));
        assertEquals(s, r.getValue());
    }
}
