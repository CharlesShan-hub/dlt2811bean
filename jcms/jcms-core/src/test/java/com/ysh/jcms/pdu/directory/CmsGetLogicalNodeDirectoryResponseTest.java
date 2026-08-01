package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.scalar.CmsSubReference;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogicalNodeDirectoryResponseTest {
    @Test
    public void roundup() {
        CmsGetLogicalNodeDirectoryResponse a = new CmsGetLogicalNodeDirectoryResponse()
            .reference(Arrays.asList(
                new CmsSubReference("fc"),
                new CmsSubReference("mx")))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryResponse b = new CmsGetLogicalNodeDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
