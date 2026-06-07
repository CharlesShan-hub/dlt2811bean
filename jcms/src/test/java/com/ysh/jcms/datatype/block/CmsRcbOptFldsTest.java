package com.ysh.jcms.datatype.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsRcbOptFlds")
class CmsRcbOptFldsTest {

    @Test
    void roundtripDefault() {
        assertEquals(new CmsRcbOptFlds(),
                     new CmsRcbOptFlds().decode(new CmsRcbOptFlds().encode()));
    }

    @Test
    void sequenceNumber() {
        CmsRcbOptFlds o = new CmsRcbOptFlds();
        o.sequence_number().value(true);
        assertEquals(o, new CmsRcbOptFlds().decode(o.encode()));
    }

    @Test
    void reportTimeStamp() {
        CmsRcbOptFlds o = new CmsRcbOptFlds();
        o.report_time_stamp().value(true);
        assertEquals(o, new CmsRcbOptFlds().decode(o.encode()));
    }

    @Test
    void reasonForInclusion() {
        CmsRcbOptFlds o = new CmsRcbOptFlds();
        o.reason_for_inclusion().value(true);
        assertEquals(o, new CmsRcbOptFlds().decode(o.encode()));
    }

    @Test
    void dataSetName() {
        CmsRcbOptFlds o = new CmsRcbOptFlds();
        o.data_set_name().value(true);
        assertEquals(o, new CmsRcbOptFlds().decode(o.encode()));
    }

    @Test
    void dataReference() {
        CmsRcbOptFlds o = new CmsRcbOptFlds();
        o.data_reference().value(true);
        assertEquals(o, new CmsRcbOptFlds().decode(o.encode()));
    }

    @Test
    void entryId() {
        CmsRcbOptFlds o = new CmsRcbOptFlds();
        o.entry_id().value(true);
        assertEquals(o, new CmsRcbOptFlds().decode(o.encode()));
    }

    @Test
    void segmentation() {
        CmsRcbOptFlds o = new CmsRcbOptFlds();
        o.segmentation().value(true);
        assertEquals(o, new CmsRcbOptFlds().decode(o.encode()));
    }

    @Test
    void allTrue() {
        CmsRcbOptFlds o = new CmsRcbOptFlds();
        o.sequence_number().value(true);
        o.report_time_stamp().value(true);
        o.reason_for_inclusion().value(true);
        o.data_set_name().value(true);
        o.data_reference().value(true);
        o.buffer_overflow().value(true);
        o.entry_id().value(true);
        o.conf_revision().value(true);
        o.segmentation().value(true);
        assertEquals(o, new CmsRcbOptFlds().decode(o.encode()));
    }
}
