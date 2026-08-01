package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.scalar.CmsObjectReference;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetServerDirectoryResponseTest {
    @Test
    public void roundup() {
        CmsGetServerDirectoryResponse a = new CmsGetServerDirectoryResponse()
            .reference(Arrays.asList(
                new CmsObjectReference("device1"),
                new CmsObjectReference("device2")))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetServerDirectoryResponse b = new CmsGetServerDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
