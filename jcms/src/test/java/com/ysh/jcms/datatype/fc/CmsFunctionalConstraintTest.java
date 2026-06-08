package com.ysh.jcms.datatype.fc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFunctionalConstraint")
class CmsFunctionalConstraintTest {

    private CmsFunctionalConstraint get() { return (CmsFunctionalConstraint)(new CmsFunctionalConstraint().test()); }

    @Test
    void roundtrip() {
        CmsFunctionalConstraint a = get().value("ST");
        CmsFunctionalConstraint b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValueEmpty() {
        assertEquals("", new String(get().value()).trim());
    }

    @Test
    void decodeOverwrites() {
        CmsFunctionalConstraint src = get().value("CO");
        CmsFunctionalConstraint target = get().decode(src.encode());
        assertEquals(src, target);
    }

    @Test
    void fixedSize() {
        assertEquals(2, CmsFunctionalConstraint.LEN);
    }
}
