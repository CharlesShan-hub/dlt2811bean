package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTriggerConditions")
class CmsTriggerConditionsTest {

    @Test
    void roundtripDefault() {
        assertEquals(new CmsTriggerConditions(),
                     new CmsTriggerConditions().decode(new CmsTriggerConditions().encode()));
    }

    @Test
    void dataChange() {
        CmsTriggerConditions t = new CmsTriggerConditions();
        t.data_change().value(true);
        assertEquals(t, new CmsTriggerConditions().decode(t.encode()));
    }

    @Test
    void qualityChange() {
        CmsTriggerConditions t = new CmsTriggerConditions();
        t.quality_change().value(true);
        assertEquals(t, new CmsTriggerConditions().decode(t.encode()));
    }

    @Test
    void integrity() {
        CmsTriggerConditions t = new CmsTriggerConditions();
        t.integrity().value(true);
        assertEquals(t, new CmsTriggerConditions().decode(t.encode()));
    }

    @Test
    void generalInterrogation() {
        CmsTriggerConditions t = new CmsTriggerConditions();
        t.general_interrogation().value(true);
        assertEquals(t, new CmsTriggerConditions().decode(t.encode()));
    }

    @Test
    void allTrue() {
        CmsTriggerConditions t = new CmsTriggerConditions();
        t.data_change().value(true);
        t.quality_change().value(true);
        t.data_update().value(true);
        t.integrity().value(true);
        t.general_interrogation().value(true);
        assertEquals(t, new CmsTriggerConditions().decode(t.encode()));
    }
}
