package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.sequence.data.CmsSubRefEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetDataDirectoryResponseTest {
    @Test
    public void roundup() {
        CmsGetDataDirectoryResponse a = new CmsGetDataDirectoryResponse()
            .dataAttribute(Arrays.asList(
                new CmsSubRefEntry().reference("subRef1").fc(CmsFC.MX),
                new CmsSubRefEntry().reference("subRef2")))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetDataDirectoryResponse b = new CmsGetDataDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
