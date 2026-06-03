package com.ysh.jcms.services.connect;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAssociationId")
class CmsAssociationIdTest {

    @Test
    void roundtrip() {
        byte[] id = {0x01, 0x02, 0x03, 0x04};
        byte[] enc = new CmsAssociationId(id).encode();
        CmsAssociationId dec = CmsAssociationId.decode(enc);
        assertArrayEquals(id, dec.get());
    }

    @Test
    void empty() {
        byte[] enc = new CmsAssociationId(new byte[0]).encode();
        CmsAssociationId dec = CmsAssociationId.decode(enc);
        assertArrayEquals(new byte[0], dec.get());
    }

    @Test
    void largeValue() {
        byte[] id = new byte[50];
        for (int i = 0; i < 50; i++) id[i] = (byte) i;
        byte[] enc = new CmsAssociationId(id).encode();
        CmsAssociationId dec = CmsAssociationId.decode(enc);
        assertArrayEquals(id, dec.get());
    }

    @Test
    void copy() {
        CmsAssociationId original = new CmsAssociationId(new byte[]{0x10, 0x20});
        CmsAssociationId cloned = original.copy();
        assertArrayEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }
}
