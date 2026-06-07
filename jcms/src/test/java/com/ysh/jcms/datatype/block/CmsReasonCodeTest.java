package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsReasonCode")
class CmsReasonCodeTest {

    @Test
    void roundtripDefault() {
        assertEquals(new CmsReasonCode(),
                     new CmsReasonCode().decode(new CmsReasonCode().encode()));
    }

    @Test
    void dataChange() {
        CmsReasonCode r = new CmsReasonCode();
        r.data_change().value(true);
        assertEquals(r, new CmsReasonCode().decode(r.encode()));
    }

    @Test
    void qualityChange() {
        CmsReasonCode r = new CmsReasonCode();
        r.quality_change().value(true);
        assertEquals(r, new CmsReasonCode().decode(r.encode()));
    }

    @Test
    void integrity() {
        CmsReasonCode r = new CmsReasonCode();
        r.integrity().value(true);
        assertEquals(r, new CmsReasonCode().decode(r.encode()));
    }

    @Test
    void applicationTrigger() {
        CmsReasonCode r = new CmsReasonCode();
        r.application_trigger().value(true);
        assertEquals(r, new CmsReasonCode().decode(r.encode()));
    }

    @Test
    void multiple() {
        CmsReasonCode r = new CmsReasonCode();
        r.data_change().value(true);
        r.data_update().value(true);
        r.general_interrogation().value(true);
        assertEquals(r, new CmsReasonCode().decode(r.encode()));
    }
}
