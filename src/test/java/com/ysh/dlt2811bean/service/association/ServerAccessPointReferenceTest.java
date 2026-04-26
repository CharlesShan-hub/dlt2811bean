package com.ysh.dlt2811bean.service.association;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ServerAccessPointReference")
class ServerAccessPointReferenceTest {

    @Test
    @DisplayName("construct with iedName and accessPoint")
    void constructWithBoth() {
        ServerAccessPointReference ref = new ServerAccessPointReference("IED1", "AP1");
        assertEquals("IED1.AP1", ref.get());
        assertEquals("IED1", ref.getIedName());
        assertEquals("AP1", ref.getAccessPoint());
    }

    @Test
    @DisplayName("construct with full reference string")
    void constructWithFullString() {
        ServerAccessPointReference ref = new ServerAccessPointReference("IED1.AP1");
        assertEquals("IED1.AP1", ref.get());
        assertEquals("IED1", ref.getIedName());
        assertEquals("AP1", ref.getAccessPoint());
    }

    @Test
    @DisplayName("set with full reference string")
    void setFullString() {
        ServerAccessPointReference ref = new ServerAccessPointReference("IED1", "AP1");
        ref.set("IED2.AP2");
        assertEquals("IED2.AP2", ref.get());
        assertEquals("IED2", ref.getIedName());
        assertEquals("AP2", ref.getAccessPoint());
    }

    @Test
    @DisplayName("setIedName preserves accessPoint")
    void setIedName() {
        ServerAccessPointReference ref = new ServerAccessPointReference("IED1", "AP1");
        ref.setIedName("IED2");
        assertEquals("IED2.AP1", ref.get());
        assertEquals("IED2", ref.getIedName());
        assertEquals("AP1", ref.getAccessPoint());
    }

    @Test
    @DisplayName("setAccessPoint preserves iedName")
    void setAccessPoint() {
        ServerAccessPointReference ref = new ServerAccessPointReference("IED1", "AP1");
        ref.setAccessPoint("AP2");
        assertEquals("IED1.AP2", ref.get());
        assertEquals("IED1", ref.getIedName());
        assertEquals("AP2", ref.getAccessPoint());
    }

    @Test
    @DisplayName("empty reference without separator throws exception")
    void emptyWithoutSeparator() {
        assertThrows(IllegalArgumentException.class,
            () -> new ServerAccessPointReference("noSeparator"));
    }

    @Test
    @DisplayName("set without separator throws exception")
    void setWithoutSeparator() {
        ServerAccessPointReference ref = new ServerAccessPointReference("IED1", "AP1");
        assertThrows(IllegalArgumentException.class,
            () -> ref.set("noSeparator"));
    }

    @Test
    @DisplayName("default constructor allows set with separator")
    void defaultConstructorThenSet() {
        ServerAccessPointReference ref = new ServerAccessPointReference();
        ref.set("IED1.AP1");
        assertEquals("IED1", ref.getIedName());
        assertEquals("AP1", ref.getAccessPoint());
    }

    @Test
    @DisplayName("iedName with separator throws exception")
    void iedNameWithSeparator() {
        assertThrows(IllegalArgumentException.class,
            () -> new ServerAccessPointReference("IED.1", "AP1"));
    }

    @Test
    @DisplayName("accessPoint with separator throws exception")
    void accessPointWithSeparator() {
        assertThrows(IllegalArgumentException.class,
            () -> new ServerAccessPointReference("IED1", "AP.1"));
    }

    @Test
    @DisplayName("setIedName with separator throws exception")
    void setIedNameWithSeparator() {
        ServerAccessPointReference ref = new ServerAccessPointReference("IED1", "AP1");
        assertThrows(IllegalArgumentException.class,
            () -> ref.setIedName("IED.2"));
    }

    @Test
    @DisplayName("setAccessPoint with separator throws exception")
    void setAccessPointWithSeparator() {
        ServerAccessPointReference ref = new ServerAccessPointReference("IED1", "AP1");
        assertThrows(IllegalArgumentException.class,
            () -> ref.setAccessPoint("AP.2"));
    }

    @Test
    @DisplayName("copy preserves value")
    void copy() {
        ServerAccessPointReference ref = new ServerAccessPointReference("IED1", "AP1");
        ServerAccessPointReference cloned = ref.copy();
        assertEquals(ref.get(), cloned.get());
        assertEquals(ref.getIedName(), cloned.getIedName());
        assertEquals(ref.getAccessPoint(), cloned.getAccessPoint());
    }
}
