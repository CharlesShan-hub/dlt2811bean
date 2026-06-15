package com.ysh.jcms.svc.sg;

import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSgTest {
    @Test
    public void select_active_sg_request() {
        CmsSelectActiveSgRequest a = new CmsSelectActiveSgRequest();
        a.reqId.value(10);
        a.sgcbReference.value("sgcbRef".getBytes());
        a.settingGroupNumber.value(1);
        byte[] encoded = a.encode();

        CmsSelectActiveSgRequest b = new CmsSelectActiveSgRequest();
        b.decode(encoded);
        assertEquals(10, b.reqId.value());
        assertArrayEquals("sgcbRef".getBytes(), b.sgcbReference.value());
    }

    @Test
    public void select_edit_sg_request() {
        CmsSelectEditSgRequest a = new CmsSelectEditSgRequest();
        a.reqId.value(20);
        a.sgcbReference.value("editSgcbRef".getBytes());
        a.settingGroupNumber.value(2);
        byte[] encoded = a.encode();

        CmsSelectEditSgRequest b = new CmsSelectEditSgRequest();
        b.decode(encoded);
        assertEquals(20, b.reqId.value());
    }

    @Test
    public void simple_error() {
        CmsGetSgcbValuesError a = new CmsGetSgcbValuesError();
        a.reqId.value(99);
        a.serviceError.value(CmsServiceError.INSTANCE_IN_USE);
        byte[] encoded = a.encode();

        CmsGetSgcbValuesError b = new CmsGetSgcbValuesError();
        b.decode(encoded);
        assertEquals(CmsServiceError.INSTANCE_IN_USE, b.serviceError.value());
    }
}
