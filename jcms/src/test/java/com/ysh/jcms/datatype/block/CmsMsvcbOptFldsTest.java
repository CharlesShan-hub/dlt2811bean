package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsMsvcbOptFlds")
class CmsMsvcbOptFldsTest {

    @Test
    void roundtripDefault() {
        assertEquals(new CmsMsvcbOptFlds(),
                     new CmsMsvcbOptFlds().decode(new CmsMsvcbOptFlds().encode()));
    }

    @Test
    void refreshTime() {
        CmsMsvcbOptFlds o = new CmsMsvcbOptFlds();
        o.refresh_time().value(true);
        assertEquals(o, new CmsMsvcbOptFlds().decode(o.encode()));
    }

    @Test
    void sampleRate() {
        CmsMsvcbOptFlds o = new CmsMsvcbOptFlds();
        o.sample_rate().value(true);
        assertEquals(o, new CmsMsvcbOptFlds().decode(o.encode()));
    }

    @Test
    void dataSetName() {
        CmsMsvcbOptFlds o = new CmsMsvcbOptFlds();
        o.data_set_name().value(true);
        assertEquals(o, new CmsMsvcbOptFlds().decode(o.encode()));
    }

    @Test
    void security() {
        CmsMsvcbOptFlds o = new CmsMsvcbOptFlds();
        o.security().value(true);
        assertEquals(o, new CmsMsvcbOptFlds().decode(o.encode()));
    }

    @Test
    void allTrue() {
        CmsMsvcbOptFlds o = new CmsMsvcbOptFlds();
        o.refresh_time().value(true);
        o.sample_rate().value(true);
        o.data_set_name().value(true);
        o.security().value(true);
        assertEquals(o, new CmsMsvcbOptFlds().decode(o.encode()));
    }
}
