package com.ysh.jcms.core.pdu.report;

import com.ysh.jcms.core.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.core.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.core.data.sequence.report.CmsSetUrcbEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetUrcbValuesRequestTest {
    @Test
    public void roundup() {
        CmsSetUrcbValuesRequest a = new CmsSetUrcbValuesRequest()
            .urcb(Arrays.asList(
                new CmsSetUrcbEntry()
                    .reference("setUrcb1")
                    .rptID("newUrcbRpt")
                    .rptEna(true)
                    .datSet("ds1")
                    .optFlds(new CmsRcbOptFlds().sequence_number(true))
                    .bufTm(30L)
                    .trgOps(new CmsTriggerConditions().data_change(true))
                    .intgPd(60L)
                    .gi(false)
                    .resv(true),
                new CmsSetUrcbEntry()
                    .reference("setUrcb2")
                    .rptEna(false)));
        byte[] encoded = a.encode();

        CmsSetUrcbValuesRequest b = new CmsSetUrcbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
