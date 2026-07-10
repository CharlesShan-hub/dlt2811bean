package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllCbValuesTest {

    @Test
    public void request_roundup_with_ref_after() {
        CmsGetAllCbValuesRequest a = new CmsGetAllCbValuesRequest().reqId(11);
        a.reference.choice.value(CmsReferenceChoice.LD_NAME);
        a.reference.altLdName.value("ld1".getBytes());
        a.acsiClass.value(CmsAcsiClass.BRCB);
        a.refAfterPresent.value(true);
        a.refAfter.value("afterRef".getBytes());
        byte[] encoded = a.encode();

        CmsGetAllCbValuesRequest b = new CmsGetAllCbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void response_roundup_with_brcb_entry() {
        CmsGetAllCbValuesResponse a = new CmsGetAllCbValuesResponse().reqId(60);
        /* SEQUENCE OF CBValueEntry — 2 个元素 */
        CmsCbValueEntry entry1 = new CmsCbValueEntry();
        entry1.reference.value("cbRef1".getBytes());
        entry1.value.choice.value(CmsCbValueChoice.BRCB);
        entry1.value.altBrcb.rptID.value("rpt01".getBytes());
        entry1.value.altBrcb.rptEna.value(true);
        entry1.value.altBrcb.confRev.value(42L);
        entry1.value.altBrcb.optFlds.sequence_number.value(true);
        entry1.value.altBrcb.sqNum.value(99);
        entry1.value.altBrcb.trgOps.data_change.value(true);
        entry1.value.altBrcb.intgPd.value(5000L);
        entry1.value.altBrcb.gi.value(false);
        entry1.value.altBrcb.purgeBuf.value(true);
        entry1.value.altBrcb.entryID.value(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});

        CmsCbValueEntry entry2 = new CmsCbValueEntry();
        entry2.reference.value("cbRef2".getBytes());
        entry2.value.choice.value(CmsCbValueChoice.GOCB);
        entry2.value.altGocb.goEna.value(true);
        entry2.value.altGocb.confRev.value(100L);
        entry2.value.altGocb.ndsCom.value(false);

        a.cbValue.add(entry1).add(entry2);
        a.moreFollows.value(false);
        byte[] encoded = a.encode();

        CmsGetAllCbValuesResponse b = new CmsGetAllCbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
        /* 校验 entry1 — BRCB */
        assertArrayEquals("cbRef1".getBytes(), b.cbValue.get(0).reference.value());
        assertEquals(CmsCbValueChoice.BRCB, b.cbValue.get(0).value.choice.value());
        assertArrayEquals("rpt01".getBytes(), b.cbValue.get(0).value.altBrcb.rptID.value());
        assertTrue(b.cbValue.get(0).value.altBrcb.rptEna.value());
        assertEquals(42L, b.cbValue.get(0).value.altBrcb.confRev.value());
        assertTrue(b.cbValue.get(0).value.altBrcb.optFlds.sequence_number.value());
        assertEquals(99, b.cbValue.get(0).value.altBrcb.sqNum.value());
        assertTrue(b.cbValue.get(0).value.altBrcb.trgOps.data_change.value());
        assertEquals(5000L, b.cbValue.get(0).value.altBrcb.intgPd.value());
        assertFalse(b.cbValue.get(0).value.altBrcb.gi.value());
        assertTrue(b.cbValue.get(0).value.altBrcb.purgeBuf.value());

        /* 校验 entry2 — GOCB */
        assertArrayEquals("cbRef2".getBytes(), b.cbValue.get(1).reference.value());
        assertEquals(CmsCbValueChoice.GOCB, b.cbValue.get(1).value.choice.value());
        assertTrue(b.cbValue.get(1).value.altGocb.goEna.value());
        assertEquals(100L, b.cbValue.get(1).value.altGocb.confRev.value());
        assertFalse(b.cbValue.get(1).value.altGocb.ndsCom.value());
    }

    @Test
    public void error_roundup() {
        CmsGetAllCbValuesError a = new CmsGetAllCbValuesError().reqId(44).serviceError(CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT);
        byte[] encoded = a.encode();

        CmsGetAllCbValuesError b = new CmsGetAllCbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
