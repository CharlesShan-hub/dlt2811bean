package com.ysh.jcms.core.pdu.report;

import com.ysh.jcms.core.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.core.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.core.data.sequence.report.CmsSetBrcbEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetBrcbValuesRequestTest {
    @Test
    public void roundup() {
        CmsSetBrcbValuesRequest a = new CmsSetBrcbValuesRequest()
            .brcb(Arrays.asList(
                new CmsSetBrcbEntry()
                    .reference("setBrcb1")
                    .rptID("newRpt")
                    .rptEna(true)
                    .datSet("ds1")
                    .optFlds(new CmsRcbOptFlds().sequence_number(true))
                    .bufTm(30L)
                    .trgOps(new CmsTriggerConditions().data_change(true))
                    .intgPd(60L)
                    .gi(false)
                    .purgeBuf(true)
                    .entryID(new byte[]{1, 2, 3, 4, 5, 6, 7, 8})
                    .resvTms(5),
                new CmsSetBrcbEntry()
                    .reference("setBrcb2")
                    .rptEna(false)));
        byte[] encoded = a.encode();

        CmsSetBrcbValuesRequest b = new CmsSetBrcbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
