package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.scalar.CmsSubReference;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogicalDeviceDirectoryResponseTest {
    @Test
    public void roundup() {
        CmsGetLogicalDeviceDirectoryResponse a = new CmsGetLogicalDeviceDirectoryResponse()
            .lnReference(Arrays.asList(
                new CmsSubReference("ln1"),
                new CmsSubReference("ln2"),
                new CmsSubReference("ln3")))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetLogicalDeviceDirectoryResponse b = new CmsGetLogicalDeviceDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
