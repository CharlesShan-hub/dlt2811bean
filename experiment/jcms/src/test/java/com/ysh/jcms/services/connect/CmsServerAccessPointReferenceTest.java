package com.ysh.jcms.services.connect;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsServerAccessPointReference")
class CmsServerAccessPointReferenceTest {

    @Test
    void constructFromParts() {
        CmsServerAccessPointReference ref = new CmsServerAccessPointReference("IED1", "AP1");
        assertEquals("IED1", ref.getIedName());
        assertEquals("AP1", ref.getAccessPoint());
        assertEquals("IED1.AP1", ref.get());
    }

    @Test
    void constructFromFullRef() {
        CmsServerAccessPointReference ref = new CmsServerAccessPointReference("StationIED.ProtectionAP");
        assertEquals("StationIED", ref.getIedName());
        assertEquals("ProtectionAP", ref.getAccessPoint());
    }

    @Test
    void setIedName() {
        CmsServerAccessPointReference ref = new CmsServerAccessPointReference("IED1", "AP1");
        ref.setIedName("IED2");
        assertEquals("IED2.AP1", ref.get());
    }

    @Test
    void setAccessPoint() {
        CmsServerAccessPointReference ref = new CmsServerAccessPointReference("IED1", "AP1");
        ref.setAccessPoint("AP2");
        assertEquals("IED1.AP2", ref.get());
    }

    @Test
    void roundtrip() {
        CmsServerAccessPointReference ref = new CmsServerAccessPointReference("MyIED", "MainAP");
        byte[] enc = ref.encode();
        CmsServerAccessPointReference dec = CmsServerAccessPointReference.decode(enc);
        assertEquals("MyIED", dec.getIedName());
        assertEquals("MainAP", dec.getAccessPoint());
    }

    @Test
    void copy() {
        CmsServerAccessPointReference original = new CmsServerAccessPointReference("IED_A", "AP_X");
        CmsServerAccessPointReference cloned = original.copy();
        assertEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }

    @Test
    void missingSeparatorThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> new CmsServerAccessPointReference("NoSeparator"));
    }

    @Test
    void iedNameWithSeparatorThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> new CmsServerAccessPointReference("IED.1", "AP"));
    }
}
