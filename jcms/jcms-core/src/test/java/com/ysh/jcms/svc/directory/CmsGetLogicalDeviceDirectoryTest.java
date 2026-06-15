package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.common.CmsSubReference;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogicalDeviceDirectoryTest {

    @Test
    public void request_roundup_without_optional() {
        CmsGetLogicalDeviceDirectoryRequest a = new CmsGetLogicalDeviceDirectoryRequest()
            .reqId(3)
            .ldNamePresent(false)
            .refAfterPresent(false);
        byte[] encoded = a.encode();

        CmsGetLogicalDeviceDirectoryRequest b = new CmsGetLogicalDeviceDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void request_roundup_with_ld_name() {
        CmsGetLogicalDeviceDirectoryRequest a = new CmsGetLogicalDeviceDirectoryRequest()
            .reqId(4)
            .ldNamePresent(true)
            .ldName("ld1".getBytes())
            .refAfterPresent(false);
        byte[] encoded = a.encode();

        CmsGetLogicalDeviceDirectoryRequest b = new CmsGetLogicalDeviceDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void response_roundup_with_array() {
        CmsGetLogicalDeviceDirectoryResponse a = new CmsGetLogicalDeviceDirectoryResponse();
        a.reqId.value(20);
        /* SEQUENCE OF SubReference — 3 个元素 */
        CmsSubReference ln1 = new CmsSubReference("ln1".getBytes());
        CmsSubReference ln2 = new CmsSubReference("ln2".getBytes());
        CmsSubReference ln3 = new CmsSubReference("ln3".getBytes());
        a.lnReference.add(ln1).add(ln2).add(ln3);
        a.moreFollows.value(true);
        byte[] encoded = a.encode();

        CmsGetLogicalDeviceDirectoryResponse b = new CmsGetLogicalDeviceDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void error_roundup() {
        CmsGetLogicalDeviceDirectoryError a = new CmsGetLogicalDeviceDirectoryError();
        a.reqId.value(88);
        a.serviceError.value(CmsServiceError.INSTANCE_IN_USE);
        byte[] encoded = a.encode();

        CmsGetLogicalDeviceDirectoryError b = new CmsGetLogicalDeviceDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
