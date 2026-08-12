package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.core.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.core.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.core.data.choice.CmsCbValueChoice;
import com.ysh.jcms.core.data.sequence.block.CmsBrcb;
import com.ysh.jcms.core.data.sequence.block.CmsGoCb;
import com.ysh.jcms.core.data.sequence.directory.CmsCbValueEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllCbValuesResponseTest {
    @Test
    public void roundup() {
        CmsGetAllCbValuesResponse a = new CmsGetAllCbValuesResponse()
            .cbValue(Arrays.asList(
                new CmsCbValueEntry()
                    .reference("cbRef1")
                    .value(new CmsCbValueChoice().altBrcb(new CmsBrcb()
                        .rptID("rpt01")
                        .rptEna(true)
                        .confRev(42L)
                        .sqNum(99)
                        .intgPd(5000L)
                        .gi(false)
                        .purgeBuf(true)
                        .entryID(new byte[]{1, 2, 3, 4, 5, 6, 7, 8})
                        .optFlds(new CmsRcbOptFlds().sequence_number(true))
                        .trgOps(new CmsTriggerConditions().data_change(true)))),
                new CmsCbValueEntry()
                    .reference("cbRef2")
                    .value(new CmsCbValueChoice().altGocb(new CmsGoCb()
                        .goEna(true)
                        .confRev(100L)
                        .ndsCom(false)))))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetAllCbValuesResponse b = new CmsGetAllCbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
        assertEquals("cbRef1", b.cbValue.get(0).reference.value());
        assertEquals(CmsCbValueChoice.BRCB, b.cbValue.get(0).value.choice());
        assertEquals("rpt01", b.cbValue.get(0).value.altBrcb.rptID.value());
        assertTrue(b.cbValue.get(0).value.altBrcb.rptEna.value());
        assertEquals(42L, b.cbValue.get(0).value.altBrcb.confRev.value());
        assertTrue(b.cbValue.get(0).value.altBrcb.optFlds.sequence_number);
        assertEquals(99, b.cbValue.get(0).value.altBrcb.sqNum.value());
        assertTrue(b.cbValue.get(0).value.altBrcb.trgOps.data_change);
        assertEquals(5000L, b.cbValue.get(0).value.altBrcb.intgPd.value());
        assertFalse(b.cbValue.get(0).value.altBrcb.gi.value());
        assertTrue(b.cbValue.get(0).value.altBrcb.purgeBuf.value());
        assertEquals("cbRef2", b.cbValue.get(1).reference.value());
        assertEquals(CmsCbValueChoice.GOCB, b.cbValue.get(1).value.choice());
        assertTrue(b.cbValue.get(1).value.altGocb.goEna.value());
        assertEquals(100L, b.cbValue.get(1).value.altGocb.confRev.value());
        assertFalse(b.cbValue.get(1).value.altGocb.ndsCom.value());
    }
}
