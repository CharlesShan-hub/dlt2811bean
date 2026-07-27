package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllDataValuesTest {

    @Test
    public void request_roundup_with_fc() {
        CmsGetAllDataValuesRequest a = new CmsGetAllDataValuesRequest();
        a.reference.choice.value(CmsReferenceChoice.LN_REFERENCE);
        a.reference.altLnReference.value("lnRef");
        a.fc(CmsFC.MX);
        byte[] encoded = a.encode();

        CmsGetAllDataValuesRequest b = new CmsGetAllDataValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void response_roundup_with_array() {
        CmsGetAllDataValuesResponse a = new CmsGetAllDataValuesResponse();
        /* SEQUENCE OF DataValueEntry — 2 个元素 */
        CmsDataValueEntry entry1 = new CmsDataValueEntry();
        entry1.reference.value("ref1");
        entry1.value.choice = CmsData.CHOICE_BOOLEAN;
        entry1.value.alt_boolean.value(true);

        CmsDataValueEntry entry2 = new CmsDataValueEntry();
        entry2.reference.value("ref2");
        entry2.value.choice = CmsData.CHOICE_INT32;
        entry2.value.alt_int32.value(12345);

        a.data.add(entry1);
        a.data.add(entry2);
        a.moreFollows = true;
        byte[] encoded = a.encode();

        CmsGetAllDataValuesResponse b = new CmsGetAllDataValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void error_roundup() {
        CmsGetAllDataValuesError a = new CmsGetAllDataValuesError().serviceError(com.ysh.jcms.data.common.CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        byte[] encoded = a.encode();

        CmsGetAllDataValuesError b = new CmsGetAllDataValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void response_nested_array_roundup() {
        /*
         * Nested arrays: Data(CHOICE_ARRAY) → SEQUENCE OF Data Each inner Data could
         * itself be an ARRAY, forming 2+ levels of nesting.
         *
         * Structure: Data (CHOICE_ARRAY) ├─ Data (CHOICE_BOOLEAN → true) └─ Data
         * (CHOICE_ARRAY) ├─ Data (CHOICE_INT32 → 1) └─ Data (CHOICE_INT32 → 2)
         */
        CmsData inner = new CmsData();
        inner.choice = CmsData.CHOICE_ARRAY;
        CmsData d1 = new CmsData();
        d1.choice = CmsData.CHOICE_INT32;
        d1.alt_int32.value(1);
        CmsData d2 = new CmsData();
        d2.choice = CmsData.CHOICE_INT32;
        d2.alt_int32.value(2);
        inner.alt_sequence = new ArrayList<>(Arrays.asList(d1, d2));

        CmsData outer = new CmsData();
        outer.choice = CmsData.CHOICE_ARRAY;
        CmsData d3 = new CmsData();
        d3.choice = CmsData.CHOICE_BOOLEAN;
        d3.alt_boolean.value(true);
        outer.alt_sequence = new ArrayList<>(Arrays.asList(d3, inner));

        CmsGetAllDataValuesResponse a = new CmsGetAllDataValuesResponse();
        a.data.add(new CmsDataValueEntry().reference("nestedRef").value(outer));
        a.moreFollows = false;

        byte[] encoded = a.encode();
        CmsGetAllDataValuesResponse b = new CmsGetAllDataValuesResponse();
        b.decode(encoded);

        CmsDataValueEntry aEntry = a.data.get(0);
        CmsDataValueEntry bEntry = b.data.get(0);
        System.out.println("a.value.choice = " + aEntry.value.choice);
        System.out.println("b.value.choice = " + bEntry.value.choice);
        System.out.println("a.value.alt_sequence.size = " + aEntry.value.alt_sequence.size());
        System.out.println("b.value.alt_sequence.size = " + bEntry.value.alt_sequence.size());
        if (aEntry.value.alt_sequence.size() > 0) {
            CmsData innerA = aEntry.value.alt_sequence.get(0);
            CmsData innerB = bEntry.value.alt_sequence.get(0);
            System.out.println("inner[0] a.choice = " + innerA.choice);
            System.out.println("inner[0] b.choice = " + innerB.choice);
            System.out.println("inner[0] a.alt_boolean.value = " + innerA.alt_boolean.value());
            System.out.println("inner[0] b.alt_boolean.value = " + innerB.alt_boolean.value());
        }

        assertEquals(a, b);
    }

    @Test
    public void response_single_data() {
        CmsData d = new CmsData();
        d.choice = CmsData.CHOICE_BOOLEAN;
        d.alt_boolean.value(true);

        CmsGetAllDataValuesResponse a = new CmsGetAllDataValuesResponse();
        a.data.add(new CmsDataValueEntry().reference("r").value(d));
        a.moreFollows = false;

        byte[] encoded = a.encode();
        CmsGetAllDataValuesResponse b = new CmsGetAllDataValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

}
