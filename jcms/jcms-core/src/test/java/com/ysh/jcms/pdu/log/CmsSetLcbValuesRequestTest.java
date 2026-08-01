package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.bitarray.CmsLcbOptFlds;
import com.ysh.jcms.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.data.sequence.log.CmsSetLcbEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetLcbValuesRequestTest {
    @Test
    public void roundup() {
        CmsSetLcbValuesRequest a = new CmsSetLcbValuesRequest()
            .lcb(Arrays.asList(
                new CmsSetLcbEntry()
                    .reference("lcb1")
                    .logEna(true)
                    .datSet("ds1")
                    .trgOps(new CmsTriggerConditions().data_change(true).integrity(true))
                    .intgPd(30L)
                    .logRef("log1")
                    .optFlds(new CmsLcbOptFlds().bit0(true))
                    .bufTm(60L),
                new CmsSetLcbEntry()
                    .reference("lcb2")
                    .logEna(false)));
        byte[] encoded = a.encode();

        CmsSetLcbValuesRequest b = new CmsSetLcbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
