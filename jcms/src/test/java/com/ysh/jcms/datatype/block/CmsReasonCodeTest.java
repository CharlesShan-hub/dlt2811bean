package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsReasonCode")
class CmsReasonCodeTest {

    private CmsReasonCode get() { return (CmsReasonCode)(new CmsReasonCode().test()); }

    @Test
    void roundtripDefault() {
        assertEquals(get(), get().decode(get().encode()));
    }

    @Test
    void dataChange() {
        CmsReasonCode r = get();
        r.data_change().value(true);
        assertEquals(r, get().decode(r.encode()));
    }

    @Test
    void qualityChange() {
        CmsReasonCode r = get();
        r.quality_change().value(true);
        assertEquals(r, get().decode(r.encode()));
    }

    @Test
    void integrity() {
        CmsReasonCode r = get();
        r.integrity().value(true);
        assertEquals(r, get().decode(r.encode()));
    }

    @Test
    void applicationTrigger() {
        CmsReasonCode r = get();
        r.application_trigger().value(true);
        assertEquals(r, get().decode(r.encode()));
    }

    @Test
    void multiple() {
        CmsReasonCode r = get();
        r.data_change().value(true);
        r.data_update().value(true);
        r.general_interrogation().value(true);
        assertEquals(r, get().decode(r.encode()));
    }
}
