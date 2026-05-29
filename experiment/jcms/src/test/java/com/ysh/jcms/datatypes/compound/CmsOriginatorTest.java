package com.ysh.jcms.datatypes.compound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsOriginator")
class CmsOriginatorTest {

    @Test
    void roundtrip() {
        byte[] ident = {0x01, 0x02, 0x03};
        CmsOriginator original = new CmsOriginator(1, ident);
        byte[] data = original.encode();
        CmsOriginator decoded = CmsOriginator.decode(data);
        assertEquals(original.getOrCat(), decoded.getOrCat());
        assertArrayEquals(original.getOrIdent(), decoded.getOrIdent());
    }

    @Test
    void copy() {
        byte[] ident = {0x01, 0x02, 0x03};
        CmsOriginator original = new CmsOriginator(1, ident);
        CmsOriginator cloned = original.copy();
        assertEquals(original.getOrCat(), cloned.getOrCat());
        assertArrayEquals(original.getOrIdent(), cloned.getOrIdent());
    }
}
