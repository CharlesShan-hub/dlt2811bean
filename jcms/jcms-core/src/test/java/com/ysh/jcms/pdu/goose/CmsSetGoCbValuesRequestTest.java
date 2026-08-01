package com.ysh.jcms.pdu.goose;

import com.ysh.jcms.data.sequence.goose.CmsSetGoCbEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetGoCbValuesRequestTest {
    @Test
    public void roundup() {
        CmsSetGoCbValuesRequest a = new CmsSetGoCbValuesRequest()
            .gocb(Arrays.asList(
                new CmsSetGoCbEntry()
                    .reference("goref1")
                    .goEna(true)
                    .goID("goID1")
                    .datSet("dsRef1"),
                new CmsSetGoCbEntry()
                    .reference("goref2")
                    .goEna(false)));
        byte[] encoded = a.encode();

        CmsSetGoCbValuesRequest b = new CmsSetGoCbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
