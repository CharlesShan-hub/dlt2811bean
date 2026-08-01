package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.sequence.data.CmsDataRefValueEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetDataValuesRequestTest {
    @Test
    public void roundup() {
        CmsSetDataValuesRequest a = new CmsSetDataValuesRequest().data(Arrays.asList(
            new CmsDataRefValueEntry()
                .reference("sv1".getBytes())
                .fc(CmsFC.ST)
                .value(new CmsData().alt_float32(1.5f)),
            new CmsDataRefValueEntry()
                .reference("sv2".getBytes())
                .value(new CmsData().alt_visible_string("hello"))));
        byte[] encoded = a.encode();

        CmsSetDataValuesRequest b = new CmsSetDataValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
