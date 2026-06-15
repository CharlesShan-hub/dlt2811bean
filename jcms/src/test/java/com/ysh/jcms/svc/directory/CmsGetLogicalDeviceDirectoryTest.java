package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.common.CmsSubReference;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogicalDeviceDirectoryTest {

    @Test
    public void request_roundtrip_without_optional() {
        CmsGetLogicalDeviceDirectoryRequest a = new CmsGetLogicalDeviceDirectoryRequest();
        a.reqId.value(3);
        a.ldNamePresent.value(false);
        a.refAfterPresent.value(false);
        byte[] encoded = a.encode();

        CmsGetLogicalDeviceDirectoryRequest b = new CmsGetLogicalDeviceDirectoryRequest();
        b.decode(encoded);
        assertEquals(3, b.reqId.value());
        assertFalse(b.ldNamePresent.value());
        assertFalse(b.refAfterPresent.value());
    }

    @Test
    public void request_roundtrip_with_ld_name() {
        CmsGetLogicalDeviceDirectoryRequest a = new CmsGetLogicalDeviceDirectoryRequest();
        a.reqId.value(4);
        a.ldNamePresent.value(true);
        a.ldName.value("ld1".getBytes());
        a.refAfterPresent.value(false);
        byte[] encoded = a.encode();

        CmsGetLogicalDeviceDirectoryRequest b = new CmsGetLogicalDeviceDirectoryRequest();
        b.decode(encoded);
        assertEquals(4, b.reqId.value());
        assertTrue(b.ldNamePresent.value());
        assertArrayEquals("ld1".getBytes(), b.ldName.value());
        assertFalse(b.refAfterPresent.value());
    }

    @Test
    public void response_roundtrip_with_array() {
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
        assertEquals(20, b.reqId.value());
        assertTrue(b.moreFollows.value());
        assertEquals(3, b.lnReference.size());
        assertArrayEquals("ln1".getBytes(), b.lnReference.get(0).value());
        assertArrayEquals("ln2".getBytes(), b.lnReference.get(1).value());
        assertArrayEquals("ln3".getBytes(), b.lnReference.get(2).value());
    }

    @Test
    public void error_roundtrip() {
        CmsGetLogicalDeviceDirectoryError a = new CmsGetLogicalDeviceDirectoryError();
        a.reqId.value(88);
        a.serviceError.value(CmsServiceError.INSTANCE_IN_USE);
        byte[] encoded = a.encode();

        CmsGetLogicalDeviceDirectoryError b = new CmsGetLogicalDeviceDirectoryError();
        b.decode(encoded);
        assertEquals(88, b.reqId.value());
        assertEquals(CmsServiceError.INSTANCE_IN_USE, b.serviceError.value());
    }
}
