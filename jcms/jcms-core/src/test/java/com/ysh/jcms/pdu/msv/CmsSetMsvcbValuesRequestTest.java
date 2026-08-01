package com.ysh.jcms.pdu.msv;

import com.ysh.jcms.data.bitarray.CmsMsvcbOptFlds;
import com.ysh.jcms.data.enumerate.CmsSmpMod;
import com.ysh.jcms.data.sequence.msv.CmsSetMsvcbEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetMsvcbValuesRequestTest {
    @Test
    public void roundup() {
        CmsSetMsvcbValuesRequest a = new CmsSetMsvcbValuesRequest()
            .msvcb(Arrays.asList(
                new CmsSetMsvcbEntry()
                    .reference("msvcb1")
                    .svEna(true)
                    .msvID("msvID1")
                    .datSet("ds1")
                    .smpMod(CmsSmpMod.SAMPLES_PER_NOMINAL_PERIOD)
                    .smpRate(4000)
                    .optFlds(new CmsMsvcbOptFlds().sample_rate(true)),
                new CmsSetMsvcbEntry()
                    .reference("msvcb2")
                    .svEna(false)));
        byte[] encoded = a.encode();

        CmsSetMsvcbValuesRequest b = new CmsSetMsvcbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
