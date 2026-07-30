package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.choice.CmsReferenceChoice;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.sequence.directory.CmsDataValueEntry;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllDataValuesTest {

    @Test
    public void request_roundup_with_fc() {
        CmsGetAllDataValuesRequest a = new CmsGetAllDataValuesRequest()
            .reference(new CmsReferenceChoice().altLnReference("lnRef"))
            .fc(CmsFC.MX);
        byte[] encoded = a.encode();

        CmsGetAllDataValuesRequest b = new CmsGetAllDataValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void response_roundup_with_array() {
        CmsGetAllDataValuesResponse a = new CmsGetAllDataValuesResponse();
        CmsDataValueEntry entry1 = new CmsDataValueEntry()
            .reference("ref1");
        entry1.value.alt_boolean(true);

        CmsDataValueEntry entry2 = new CmsDataValueEntry()
            .reference("ref2");
        entry2.value.alt_int32(12345);

        a.data.add(entry1);
        a.data.add(entry2);
        a.moreFollows(true);
        byte[] encoded = a.encode();

        CmsGetAllDataValuesResponse b = new CmsGetAllDataValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void error_roundup() {
        CmsGetAllDataValuesError a = new CmsGetAllDataValuesError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        byte[] encoded = a.encode();

        CmsGetAllDataValuesError b = new CmsGetAllDataValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void response_nested_array_roundup() {
        CmsData inner = new CmsData().choice(CmsData.CHOICE_ARRAY);
        inner.alt_sequence.add(new CmsData().alt_int32(1));
        inner.alt_sequence.add(new CmsData().alt_int32(2));

        CmsData outer = new CmsData().choice(CmsData.CHOICE_ARRAY);
        outer.alt_sequence.add(new CmsData().alt_boolean(true));
        outer.alt_sequence.add(inner);

        CmsGetAllDataValuesResponse a = new CmsGetAllDataValuesResponse();
        a.data.add(new CmsDataValueEntry().reference("nestedRef").value(outer));
        a.moreFollows(false);

        byte[] encoded = a.encode();
        CmsGetAllDataValuesResponse b = new CmsGetAllDataValuesResponse();
        b.decode(encoded);

        CmsDataValueEntry aEntry = a.data.get(0);
        CmsDataValueEntry bEntry = b.data.get(0);
        assertEquals(aEntry.reference.value(), bEntry.reference.value());
        assertEquals(aEntry.value.choice(), bEntry.value.choice());
        assertEquals(aEntry.value.alt_sequence.size(), bEntry.value.alt_sequence.size());
        for (int i = 0; i < aEntry.value.alt_sequence.size(); i++) {
            CmsData ia = aEntry.value.alt_sequence.get(i), ib = bEntry.value.alt_sequence.get(i);
            assertEquals(ia.choice(), ib.choice());
            if (ia.choice() == CmsData.CHOICE_BOOLEAN)
                assertEquals(ia.alt_boolean.value(), ib.alt_boolean.value());
            if (ia.choice() == CmsData.CHOICE_INT32)
                assertEquals(ia.alt_int32.value(), ib.alt_int32.value());
        }
        assertEquals(a.moreFollows.value(), b.moreFollows.value());
    }

    @Test
    public void response_single_data() {
        CmsGetAllDataValuesResponse a = new CmsGetAllDataValuesResponse();
        a.data.add(new CmsDataValueEntry()
            .reference("r")
            .value(new CmsData().alt_boolean(true)));
        a.moreFollows(false);

        byte[] encoded = a.encode();
        CmsGetAllDataValuesResponse b = new CmsGetAllDataValuesResponse();
        b.decode(encoded);
        assertEquals(a.data.size(), b.data.size());
        assertEquals(a.data.get(0).reference.value(), b.data.get(0).reference.value());
        assertEquals(a.data.get(0).value.choice(), b.data.get(0).value.choice());
        assertEquals(a.data.get(0).value.alt_boolean.value(), b.data.get(0).value.alt_boolean.value());
        assertEquals(a.moreFollows.value(), b.moreFollows.value());
    }
}
