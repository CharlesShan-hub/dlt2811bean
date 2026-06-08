package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsMsvcbOptFlds")
class CmsMsvcbOptFldsTest {

    private CmsMsvcbOptFlds get() { return (CmsMsvcbOptFlds)(new CmsMsvcbOptFlds().test()); }

    @Test
    void roundtripDefault() {
        assertEquals(get(), get().decode(get().encode()));
    }

    @Test
    void refreshTime() {
        CmsMsvcbOptFlds o = get();
        o.refresh_time().value(true);
        assertEquals(o, get().decode(o.encode()));
    }

    @Test
    void sampleRate() {
        CmsMsvcbOptFlds o = get();
        o.sample_rate().value(true);
        assertEquals(o, get().decode(o.encode()));
    }

    @Test
    void dataSetName() {
        CmsMsvcbOptFlds o = get();
        o.data_set_name().value(true);
        assertEquals(o, get().decode(o.encode()));
    }

    @Test
    void security() {
        CmsMsvcbOptFlds o = get();
        o.security().value(true);
        assertEquals(o, get().decode(o.encode()));
    }

    @Test
    void allTrue() {
        CmsMsvcbOptFlds o = get();
        o.refresh_time().value(true);
        o.sample_rate().value(true);
        o.data_set_name().value(true);
        o.security().value(true);
        assertEquals(o, get().decode(o.encode()));
    }
}
