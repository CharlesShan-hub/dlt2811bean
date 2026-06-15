package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllDataDefinitionTest {

    @Test
    public void request_roundtrip_without_optional() {
        CmsGetAllDataDefinitionRequest a = new CmsGetAllDataDefinitionRequest();
        a.reqId.value(8);
        a.reference.choice.value(CmsReferenceChoice.LD_NAME);
        a.reference.altLdName.value("ld1".getBytes());
        a.fcPresent.value(false);
        a.refAfterPresent.value(false);
        byte[] encoded = a.encode();

        CmsGetAllDataDefinitionRequest b = new CmsGetAllDataDefinitionRequest();
        b.decode(encoded);
        assertEquals(8, b.reqId.value());
        assertEquals(CmsReferenceChoice.LD_NAME, b.reference.choice.value());
        assertArrayEquals("ld1".getBytes(), b.reference.altLdName.value());
        assertFalse(b.fcPresent.value());
        assertFalse(b.refAfterPresent.value());
    }

    @Test
    public void request_roundtrip_with_all_optional() {
        CmsGetAllDataDefinitionRequest a = new CmsGetAllDataDefinitionRequest();
        a.reqId.value(9);
        a.reference.choice.value(CmsReferenceChoice.LN_REFERENCE);
        a.reference.altLnReference.value("lnRef".getBytes());
        a.fcPresent.value(true);
        a.fc.value("ST".getBytes());
        a.refAfterPresent.value(true);
        a.refAfter.value("after".getBytes());
        byte[] encoded = a.encode();

        CmsGetAllDataDefinitionRequest b = new CmsGetAllDataDefinitionRequest();
        b.decode(encoded);
        assertEquals(9, b.reqId.value());
        assertArrayEquals("lnRef".getBytes(), b.reference.altLnReference.value());
        assertTrue(b.fcPresent.value());
        assertArrayEquals("ST".getBytes(), b.fc.value());
        assertTrue(b.refAfterPresent.value());
        assertArrayEquals("after".getBytes(), b.refAfter.value());
    }

    @Test
    public void error_roundtrip() {
        CmsGetAllDataDefinitionError a = new CmsGetAllDataDefinitionError();
        a.reqId.value(55);
        a.serviceError.value(CmsServiceError.TYPE_CONFLICT);
        byte[] encoded = a.encode();

        CmsGetAllDataDefinitionError b = new CmsGetAllDataDefinitionError();
        b.decode(encoded);
        assertEquals(55, b.reqId.value());
        assertEquals(CmsServiceError.TYPE_CONFLICT, b.serviceError.value());
    }
}
