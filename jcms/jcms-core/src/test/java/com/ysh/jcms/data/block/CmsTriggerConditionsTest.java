package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsTriggerConditionsTest {
    @Test
    public void roundtrip() {
        CmsTriggerConditions a = new CmsTriggerConditions()
            .data_change(true)
            .integrity(true);
        byte[] encoded = a.encode();
        CmsTriggerConditions b = new CmsTriggerConditions();
        b.decode(encoded);
        assertTrue(b.data_change.value());
        assertTrue(b.integrity.value());
    }
}
