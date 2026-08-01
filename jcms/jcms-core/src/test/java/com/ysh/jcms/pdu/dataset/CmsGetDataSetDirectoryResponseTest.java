package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.sequence.dataset.CmsDataRefFcEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetDataSetDirectoryResponseTest {
    @Test
    public void roundup() {
        CmsGetDataSetDirectoryResponse a = new CmsGetDataSetDirectoryResponse()
            .memberData(Arrays.asList(
                new CmsDataRefFcEntry().reference("ref1".getBytes()).fc(CmsFC.ST),
                new CmsDataRefFcEntry().reference("ref2".getBytes()).fc(CmsFC.MX)))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetDataSetDirectoryResponse b = new CmsGetDataSetDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
