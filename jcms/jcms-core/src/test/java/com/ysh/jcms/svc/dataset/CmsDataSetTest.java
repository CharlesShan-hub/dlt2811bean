package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsDataSetTest {
    @Test
    public void create_dataset_request() {
        CmsCreateDataSetRequest a = new CmsCreateDataSetRequest();
        a.reqId.value(1);
        a.datasetReference.value("dsRef".getBytes());
        a.refAfterPresent.value(false);
        CmsDataRefFcEntry m1 = new CmsDataRefFcEntry();
        m1.reference.value("ref1".getBytes());
        m1.fc.value("MX".getBytes());
        a.memberData.add(m1);
        byte[] encoded = a.encode();

        CmsCreateDataSetRequest b = new CmsCreateDataSetRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void delete_dataset_request() {
        CmsDeleteDataSetRequest a = new CmsDeleteDataSetRequest();
        a.reqId.value(10);
        a.datasetReference.value("dsRef".getBytes());
        byte[] encoded = a.encode();

        CmsDeleteDataSetRequest b = new CmsDeleteDataSetRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void set_dataset_values_request() {
        CmsSetDataSetValuesRequest a = new CmsSetDataSetValuesRequest();
        a.reqId.value(40);
        a.datasetReference.value("dsRef".getBytes());
        a.refAfterPresent.value(false);
        CmsData d1 = new CmsData();
        d1.choice.value(CmsData.CHOICE_BOOLEAN);
        d1.alt_boolean.value(true);
        a.value.add(d1);
        byte[] encoded = a.encode();

        CmsSetDataSetValuesRequest b = new CmsSetDataSetValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void simple_error() {
        CmsCreateDataSetError a = new CmsCreateDataSetError();
        a.reqId.value(99);
        a.serviceError.value(CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT);
        byte[] encoded = a.encode();
        CmsCreateDataSetError b = new CmsCreateDataSetError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
