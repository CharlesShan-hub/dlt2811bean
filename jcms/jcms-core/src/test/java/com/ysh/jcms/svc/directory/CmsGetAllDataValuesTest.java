package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllDataValuesTest {

    @Test
    public void request_roundup_with_fc() {
        CmsGetAllDataValuesRequest a = new CmsGetAllDataValuesRequest();
        a.reqId.value(7);
        a.reference.choice.value(CmsReferenceChoice.LN_REFERENCE);
        a.reference.altLnReference.value("lnRef".getBytes());
        a.fcPresent.value(true);
        a.fc.value(CmsFC.MX);
        a.refAfterPresent.value(false);
        byte[] encoded = a.encode();

        CmsGetAllDataValuesRequest b = new CmsGetAllDataValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void response_roundup_with_array() {
        CmsGetAllDataValuesResponse a = new CmsGetAllDataValuesResponse();
        a.reqId.value(40);
        /* SEQUENCE OF DataValueEntry — 2 个元素 */
        CmsDataValueEntry entry1 = new CmsDataValueEntry();
        entry1.reference.value("ref1".getBytes());
        entry1.value.choice.value(CmsData.CHOICE_BOOLEAN);
        entry1.value.alt_boolean.value(true);

        CmsDataValueEntry entry2 = new CmsDataValueEntry();
        entry2.reference.value("ref2".getBytes());
        entry2.value.choice.value(CmsData.CHOICE_INT32);
        entry2.value.alt_int32.value(12345);

        a.data.add(entry1).add(entry2);
        a.moreFollows.value(true);
        byte[] encoded = a.encode();

        CmsGetAllDataValuesResponse b = new CmsGetAllDataValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void error_roundup() {
        CmsGetAllDataValuesError a = new CmsGetAllDataValuesError();
        a.reqId.value(66);
        a.serviceError.value(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        byte[] encoded = a.encode();

        CmsGetAllDataValuesError b = new CmsGetAllDataValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void response_nested_array_roundup() {
        /*
         * Nested arrays: Data(CHOICE_ARRAY) → SEQUENCE OF Data Each inner Data could
         * itself be an ARRAY, forming 2+ levels of CmsArray nesting — exercises
         * multi-level CMS_RETRY.
         *
         * Structure: Data (CHOICE_ARRAY) ├─ Data (CHOICE_BOOLEAN → true) └─ Data
         * (CHOICE_ARRAY) ├─ Data (CHOICE_INT32 → 1) └─ Data (CHOICE_INT32 → 2)
         */
        CmsData inner = new CmsData();
        inner.choice.value(CmsData.CHOICE_ARRAY);
        CmsData d1 = new CmsData();
        d1.choice.value(CmsData.CHOICE_INT32);
        d1.alt_int32.value(1);
        CmsData d2 = new CmsData();
        d2.choice.value(CmsData.CHOICE_INT32);
        d2.alt_int32.value(2);
        inner.alt_sequence.add(d1).add(d2);

        CmsData outer = new CmsData();
        outer.choice.value(CmsData.CHOICE_ARRAY);
        CmsData d3 = new CmsData();
        d3.choice.value(CmsData.CHOICE_BOOLEAN);
        d3.alt_boolean.value(true);
        outer.alt_sequence.add(d3).add(inner);

        CmsGetAllDataValuesResponse a = new CmsGetAllDataValuesResponse();
        a.reqId.value(50);
        a.data.add(new CmsDataValueEntry().reference("nestedRef".getBytes()).value(outer));
        a.moreFollows.value(false);

        byte[] encoded = a.encode();
        CmsGetAllDataValuesResponse b = new CmsGetAllDataValuesResponse();
        b.decode(encoded);

        CmsDataValueEntry aEntry = a.data.get(0);
        CmsDataValueEntry bEntry = b.data.get(0);
        System.out.println("a.value.choice = " + aEntry.value.choice.value());
        System.out.println("b.value.choice = " + bEntry.value.choice.value());
        System.out.println("a.value.alt_sequence.count = " + aEntry.value.alt_sequence.count);
        System.out.println("b.value.alt_sequence.count = " + bEntry.value.alt_sequence.count);
        System.out.println("a.value.alt_sequence.items.size = " + aEntry.value.alt_sequence.items.size());
        System.out.println("b.value.alt_sequence.items.size = " + bEntry.value.alt_sequence.items.size());
        if (aEntry.value.alt_sequence.items.size() > 0) {
            CmsData innerA = aEntry.value.alt_sequence.items.get(0);
            CmsData innerB = bEntry.value.alt_sequence.items.get(0);
            System.out.println("inner[0] a.choice = " + innerA.choice.value());
            System.out.println("inner[0] b.choice = " + innerB.choice.value());
            System.out.println("inner[0] a.alt_boolean.value = " + innerA.alt_boolean.value());
            System.out.println("inner[0] b.alt_boolean.value = " + innerB.alt_boolean.value());
        }

        assertEquals(a, b);
    }

    @Test
    public void response_single_data() {
        CmsData d = new CmsData();
        d.choice.value(CmsData.CHOICE_BOOLEAN);
        d.alt_boolean.value(true);

        CmsGetAllDataValuesResponse a = new CmsGetAllDataValuesResponse();
        a.reqId.value(1);
        a.data.add(new CmsDataValueEntry().reference("r".getBytes()).value(d));
        a.moreFollows.value(false);

        byte[] encoded = a.encode();
        CmsGetAllDataValuesResponse b = new CmsGetAllDataValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

}
