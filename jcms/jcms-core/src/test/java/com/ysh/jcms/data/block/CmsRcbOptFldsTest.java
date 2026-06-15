package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsRcbOptFldsTest {
    @Test
    public void roundtrip() {
        CmsRcbOptFlds a = new CmsRcbOptFlds()
            .sequence_number(true)
            .report_time_stamp(true)
            .data_set_name(true);
        byte[] encoded = a.encode();
        CmsRcbOptFlds b = new CmsRcbOptFlds();
        b.decode(encoded);
        assertTrue(b.sequence_number.value());
        assertTrue(b.report_time_stamp.value());
        assertTrue(b.data_set_name.value());
    }
}
