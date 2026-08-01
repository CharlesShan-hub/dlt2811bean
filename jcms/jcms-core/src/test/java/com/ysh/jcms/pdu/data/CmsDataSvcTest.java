package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.sequence.data.CmsDataRefEntry;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsDataSvcTest {

    @Test
    public void get_data_dir_request() {
        CmsGetDataDirectoryRequest a = new CmsGetDataDirectoryRequest()
            .dataReference("dataRef")
            .referenceAfter("after");
        byte[] encoded = a.encode();

        CmsGetDataDirectoryRequest b = new CmsGetDataDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_data_values_request() {
        List<CmsDataRefEntry> list = new ArrayList<>();
        list.add(new CmsDataRefEntry().reference("dv1".getBytes()).fc(CmsFC.MX));
        list.add(new CmsDataRefEntry().reference("dv2".getBytes()));
        CmsGetDataValuesRequest a = new CmsGetDataValuesRequest().data(list);
        byte[] encoded = a.encode();

        CmsGetDataValuesRequest b = new CmsGetDataValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void simple_error() {
        CmsGetDataDirectoryError a = new CmsGetDataDirectoryError(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsGetDataDirectoryError b = new CmsGetDataDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
