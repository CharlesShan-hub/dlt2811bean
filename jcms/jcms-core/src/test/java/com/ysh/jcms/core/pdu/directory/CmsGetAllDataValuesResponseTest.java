package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.sequence.directory.CmsDataValueEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllDataValuesResponseTest {
    @Test
    public void roundupWithArray() {
        CmsDataValueEntry entry1 = new CmsDataValueEntry().reference("ref1");
        entry1.value.alt_boolean(true);
        CmsDataValueEntry entry2 = new CmsDataValueEntry().reference("ref2");
        entry2.value.alt_int32(12345);
        CmsGetAllDataValuesResponse a = new CmsGetAllDataValuesResponse()
            .data(Arrays.asList(entry1, entry2))
            .moreFollows(true);
        byte[] encoded = a.encode();

        CmsGetAllDataValuesResponse b = new CmsGetAllDataValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void nestedArrayRoundup() {
        CmsData inner = new CmsData().choice(CmsData.CHOICE_ARRAY);
        inner.alt_sequence.add(new CmsData().alt_int32(1));
        inner.alt_sequence.add(new CmsData().alt_int32(2));

        CmsData outer = new CmsData().choice(CmsData.CHOICE_ARRAY);
        outer.alt_sequence.add(new CmsData().alt_boolean(true));
        outer.alt_sequence.add(inner);

        CmsGetAllDataValuesResponse a = new CmsGetAllDataValuesResponse()
            .data(Arrays.asList(new CmsDataValueEntry().reference("nestedRef").value(outer)))
            .moreFollows(false);

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
    public void singleData() {
        CmsGetAllDataValuesResponse a = new CmsGetAllDataValuesResponse()
            .data(Arrays.asList(new CmsDataValueEntry()
                .reference("r")
                .value(new CmsData().alt_boolean(true))))
            .moreFollows(false);

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
