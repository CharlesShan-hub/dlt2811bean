package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllDataValuesTest {

    @Test
    public void request_roundup_with_fc() {
        CmsGetAllDataValuesRequest a = new CmsGetAllDataValuesRequest();
        a.reqId.value(7);
        a.reference.choice.value(CmsReferenceChoice.LN_REFERENCE);
        a.reference.altLnReference.value("lnRef".getBytes());
        a.fcPresent.value(true);
        a.fc.value("MX".getBytes());
        a.refAfterPresent.value(false);
        byte[] encoded = a.encode();

        CmsGetAllDataValuesRequest b = new CmsGetAllDataValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void response_roundup_with_array() {
        CmsGetAllDataValuesResponse a = new CmsGetAllDataValuesResponse();
        a.reqId.value(40);
        /* SEQUENCE OF DataValueEntry — 2 个元素 */
        CmsDataValueEntry entry1 = new CmsDataValueEntry();
        entry1.reference.value("ref1".getBytes());
        entry1.value.choice.value(CmsData.CHOICE_BOOLEAN);
        entry1.value.alt_boolean.value(true);

        CmsDataValueEntry entry2 = new CmsDataValueEntry();
        entry2.reference.value("ref2".getBytes());
        entry2.value.choice.value(CmsData.CHOICE_INT32);
        entry2.value.alt_int32.value(12345);

        a.data.add(entry1).add(entry2);
        a.moreFollows.value(true);
        byte[] encoded = a.encode();

        CmsGetAllDataValuesResponse b = new CmsGetAllDataValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void error_roundup() {
        CmsGetAllDataValuesError a = new CmsGetAllDataValuesError();
        a.reqId.value(66);
        a.serviceError.value(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        byte[] encoded = a.encode();

        CmsGetAllDataValuesError b = new CmsGetAllDataValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
