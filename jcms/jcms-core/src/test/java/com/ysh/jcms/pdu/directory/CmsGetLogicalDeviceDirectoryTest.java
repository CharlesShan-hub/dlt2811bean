package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.sequence.common.CmsSubReference;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogicalDeviceDirectoryTest {

    @Test
    public void request_roundup_without_optional() {
        CmsGetLogicalDeviceDirectoryRequest a = new CmsGetLogicalDeviceDirectoryRequest();
        byte[] encoded = a.encode();

        CmsGetLogicalDeviceDirectoryRequest b = new CmsGetLogicalDeviceDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void request_roundup_with_ld_name() {
        CmsGetLogicalDeviceDirectoryRequest a = new CmsGetLogicalDeviceDirectoryRequest()
                .ldName("ld1");
        byte[] encoded = a.encode();

        CmsGetLogicalDeviceDirectoryRequest b = new CmsGetLogicalDeviceDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void response_roundup_with_array() {
        CmsGetLogicalDeviceDirectoryResponse a = new CmsGetLogicalDeviceDirectoryResponse();
        /* SEQUENCE OF SubReference — 3 个元素 */
        CmsSubReference ln1 = new CmsSubReference("ln1");
        CmsSubReference ln2 = new CmsSubReference("ln2");
        CmsSubReference ln3 = new CmsSubReference("ln3");
        a.lnReference.add(ln1);
        a.lnReference.add(ln2);
        a.lnReference.add(ln3);
        a.moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetLogicalDeviceDirectoryResponse b = new CmsGetLogicalDeviceDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void error_roundup() {
        CmsGetLogicalDeviceDirectoryError a = new CmsGetLogicalDeviceDirectoryError(CmsServiceError.INSTANCE_IN_USE);
        byte[] encoded = a.encode();

        CmsGetLogicalDeviceDirectoryError b = new CmsGetLogicalDeviceDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
