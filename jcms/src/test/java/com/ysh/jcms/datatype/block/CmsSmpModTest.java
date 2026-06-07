package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSmpMod")
class CmsSmpModTest {

    @Test
    void samplesPerNominalPeriod() {
        assertEquals(new CmsSmpMod().value(CmsSmpMod.SAMPLES_PER_NOMINAL_PERIOD),
                     new CmsSmpMod().decode(new CmsSmpMod().value(CmsSmpMod.SAMPLES_PER_NOMINAL_PERIOD).encode()));
    }

    @Test
    void samplesPerSecond() {
        assertEquals(new CmsSmpMod().value(CmsSmpMod.SAMPLES_PER_SECOND),
                     new CmsSmpMod().decode(new CmsSmpMod().value(CmsSmpMod.SAMPLES_PER_SECOND).encode()));
    }

    @Test
    void secondsPerSample() {
        assertEquals(new CmsSmpMod().value(CmsSmpMod.SECONDS_PER_SAMPLE),
                     new CmsSmpMod().decode(new CmsSmpMod().value(CmsSmpMod.SECONDS_PER_SAMPLE).encode()));
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsSmpMod().value());
    }
}
