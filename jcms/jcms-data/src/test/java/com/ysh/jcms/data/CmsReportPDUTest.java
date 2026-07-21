// Auto-generated. Tests for CmsReportPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsReportPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsReportPDU obj = new CmsReportPDU();
        assertNull(obj.rpt_id);
        assertEquals(0, obj.opt_flds);
        assertNull(obj.sq_num);
        assertNull(obj.sub_seq_num);
        assertNull(obj.more_segments_follow);
        assertNull(obj.data_set);
        assertNull(obj.buf_ovfl);
        assertNull(obj.conf_rev);
        assertNull(obj.entry);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsReportPDU obj = new CmsReportPDU();
        obj.rpt_id = "test";
        obj.opt_flds = 42;
        obj.sq_num = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsReportPDU d = MAPPER.readValue(json, CmsReportPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsReportPDU obj = new CmsReportPDU();
        obj.rpt_id = "test";
        obj.opt_flds = 42;
        obj.sq_num = 42;
        byte[] data = obj.encode("uper");
        CmsReportPDU d = CmsReportPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
