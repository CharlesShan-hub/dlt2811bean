package com.ysh.jcms.datatype.fc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFunctionalConstraint")
class CmsFunctionalConstraintTest {

    @Test
    void roundtrip() {
        CmsFunctionalConstraint original = new CmsFunctionalConstraint().value("ST");
        assertEquals(original, new CmsFunctionalConstraint().decode(original.encode()));
    }

    @Test
    void defaultValueEmpty() {
        CmsFunctionalConstraint fc = new CmsFunctionalConstraint();
        assertEquals("", new String(fc.value()).trim());
    }

    @Test
    void decodeOverwrites() {
        CmsFunctionalConstraint target = new CmsFunctionalConstraint().value("MX");
        target.decode(new CmsFunctionalConstraint().value("CO").encode());
        assertEquals("CO", new String(target.value()).trim());
    }

    @Test
    void fixedSize() {
        assertEquals(2, CmsFunctionalConstraint.LEN);
    }
}
