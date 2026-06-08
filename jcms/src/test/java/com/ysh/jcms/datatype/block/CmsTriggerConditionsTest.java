package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTriggerConditions")
class CmsTriggerConditionsTest {

    private CmsTriggerConditions get() { return (CmsTriggerConditions)(new CmsTriggerConditions().test()); }

    @Test
    void roundtripDefault() {
        assertEquals(get(), get().decode(get().encode()));
    }

    @Test
    void dataChange() {
        CmsTriggerConditions t = get();
        t.data_change().value(true);
        assertEquals(t, get().decode(t.encode()));
    }

    @Test
    void qualityChange() {
        CmsTriggerConditions t = get();
        t.quality_change().value(true);
        assertEquals(t, get().decode(t.encode()));
    }

    @Test
    void integrity() {
        CmsTriggerConditions t = get();
        t.integrity().value(true);
        assertEquals(t, get().decode(t.encode()));
    }

    @Test
    void generalInterrogation() {
        CmsTriggerConditions t = get();
        t.general_interrogation().value(true);
        assertEquals(t, get().decode(t.encode()));
    }

    @Test
    void allTrue() {
        CmsTriggerConditions t = get();
        t.data_change().value(true);
        t.quality_change().value(true);
        t.data_update().value(true);
        t.integrity().value(true);
        t.general_interrogation().value(true);
        assertEquals(t, get().decode(t.encode()));
    }
}
