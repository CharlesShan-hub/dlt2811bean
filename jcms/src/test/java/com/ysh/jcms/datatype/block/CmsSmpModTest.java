package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSmpMod")
class CmsSmpModTest {

    private CmsSmpMod get() { return (CmsSmpMod)(new CmsSmpMod().test()); }

    @Test
    void samplesPerNominalPeriod() {
        CmsSmpMod a = get().value(CmsSmpMod.SAMPLES_PER_NOMINAL_PERIOD);
        CmsSmpMod b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void samplesPerSecond() {
        CmsSmpMod a = get().value(CmsSmpMod.SAMPLES_PER_SECOND);
        CmsSmpMod b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void secondsPerSample() {
        CmsSmpMod a = get().value(CmsSmpMod.SECONDS_PER_SAMPLE);
        CmsSmpMod b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValue() {
        assertEquals(0, get().value());
    }
}
