package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsTriggerConditionsTest {
    @Test
    public void roundup() {
        CmsTriggerConditions a = new CmsTriggerConditions()
            .data_change(true)
            .integrity(true);
        byte[] encoded = a.encode();
        CmsTriggerConditions b = new CmsTriggerConditions();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
