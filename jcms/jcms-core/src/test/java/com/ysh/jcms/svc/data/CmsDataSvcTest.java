package com.ysh.jcms.svc.data;

import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.fc.CmsFC;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsDataSvcTest {
    @Test
    public void get_data_dir_request() {
        CmsGetDataDirectoryRequest a = new CmsGetDataDirectoryRequest();
        a.reqId.value(1);
        a.dataReference.value("dataRef".getBytes());
        a.refAfterPresent.value(false);
        byte[] encoded = a.encode();

        CmsGetDataDirectoryRequest b = new CmsGetDataDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_data_values_request() {
        CmsGetDataValuesRequest a = new CmsGetDataValuesRequest();
        a.reqId.value(30);
        CmsDataRefEntry r1 = new CmsDataRefEntry();
        r1.reference.value("dv1".getBytes());
        r1.fcPresent.value(true);
        r1.fc.value(CmsFC.MX);
        a.data.add(r1);
        byte[] encoded = a.encode();

        CmsGetDataValuesRequest b = new CmsGetDataValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void simple_error() {
        CmsGetDataDirectoryError a = new CmsGetDataDirectoryError();
        a.reqId.value(99);
        a.serviceError.value(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsGetDataDirectoryError b = new CmsGetDataDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
