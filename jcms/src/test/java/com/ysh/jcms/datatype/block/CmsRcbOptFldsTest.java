package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsRcbOptFlds")
class CmsRcbOptFldsTest {

    private CmsRcbOptFlds get() { return (CmsRcbOptFlds)(new CmsRcbOptFlds().test()); }

    @Test
    void roundtripDefault() {
        assertEquals(get(), get().decode(get().encode()));
    }

    @Test
    void sequenceNumber() {
        CmsRcbOptFlds o = get();
        o.sequence_number().value(true);
        assertEquals(o, get().decode(o.encode()));
    }

    @Test
    void reportTimeStamp() {
        CmsRcbOptFlds o = get();
        o.report_time_stamp().value(true);
        assertEquals(o, get().decode(o.encode()));
    }

    @Test
    void reasonForInclusion() {
        CmsRcbOptFlds o = get();
        o.reason_for_inclusion().value(true);
        assertEquals(o, get().decode(o.encode()));
    }

    @Test
    void dataSetName() {
        CmsRcbOptFlds o = get();
        o.data_set_name().value(true);
        assertEquals(o, get().decode(o.encode()));
    }

    @Test
    void dataReference() {
        CmsRcbOptFlds o = get();
        o.data_reference().value(true);
        assertEquals(o, get().decode(o.encode()));
    }

    @Test
    void entryId() {
        CmsRcbOptFlds o = get();
        o.entry_id().value(true);
        assertEquals(o, get().decode(o.encode()));
    }

    @Test
    void segmentation() {
        CmsRcbOptFlds o = get();
        o.segmentation().value(true);
        assertEquals(o, get().decode(o.encode()));
    }

    @Test
    void allTrue() {
        CmsRcbOptFlds o = get();
        o.sequence_number().value(true);
        o.report_time_stamp().value(true);
        o.reason_for_inclusion().value(true);
        o.data_set_name().value(true);
        o.data_reference().value(true);
        o.buffer_overflow().value(true);
        o.entry_id().value(true);
        o.conf_revision().value(true);
        o.segmentation().value(true);
        assertEquals(o, get().decode(o.encode()));
    }
}
