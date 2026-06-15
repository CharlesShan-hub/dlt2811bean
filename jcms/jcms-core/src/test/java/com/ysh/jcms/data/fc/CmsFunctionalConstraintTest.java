package com.ysh.jcms.data.fc;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsFunctionalConstraintTest {
    @Test
    public void roundup() {
        CmsFunctionalConstraint a = new CmsFunctionalConstraint();
        a.value("ST".getBytes());
        byte[] encoded = a.encode();
        CmsFunctionalConstraint b = new CmsFunctionalConstraint();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
