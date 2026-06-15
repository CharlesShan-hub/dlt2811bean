package com.ysh.dlt2811bean.transport.app.directory;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalDeviceDirectory;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetLogicalDeviceDirectory Loopback Test")
class GetLogicalDeviceDirectoryLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("GetLogicalDeviceDirectory without ldName returns all LNs with full references")
    void withoutLdName() throws Exception {
        associate();

        CmsApdu response = client.getLogicalDeviceDirectory();
        //log.info("Response (no ldName): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetLogicalDeviceDirectory asdu = (CmsGetLogicalDeviceDirectory) response.getAsdu();
        assertNotNull(asdu.lnReference());
        assertTrue(asdu.lnReference().size() > 0);

        //log.info("Logical nodes:");
        //for (int i = 0; i < asdu.lnReference().size(); i++) {
        //    log.info("  [{}] {}", i, asdu.lnReference().get(i).get());
        //}
    }

    @Test
    @DisplayName("GetLogicalDeviceDirectory with ldName returns short LN names")
    void withLdName() throws Exception {
        associate();

        CmsApdu response = client.getLogicalDeviceDirectory("C1");
        //log.info("Response (ldName=C1): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetLogicalDeviceDirectory asdu = (CmsGetLogicalDeviceDirectory) response.getAsdu();
        assertNotNull(asdu.lnReference());
        assertTrue(asdu.lnReference().size() > 0);

        //log.info("Logical nodes under C1:");
        //for (int i = 0; i < asdu.lnReference().size(); i++) {
        //    log.info("  [{}] {}", i, asdu.lnReference().get(i).get());
        //}
    }

    @Test
    @DisplayName("GetLogicalDeviceDirectory with referenceAfter returns entries after that position")
    void withReferenceAfter() throws Exception {
        associate();

        // First get all entries to know the total count
        CmsApdu allResponse = client.getLogicalDeviceDirectory("C1");
        CmsGetLogicalDeviceDirectory allAsdu = (CmsGetLogicalDeviceDirectory) allResponse.getAsdu();
        int totalCount = allAsdu.lnReference().size();
        //log.info("Total entries under C1: {}", totalCount);

        // Now call with referenceAfter pointing to an entry in the middle
        // Expected order (from SCD): LLN0, LPHD1, CSWI1, CSWI2, MMXU1, TVTR1
        String afterRef = "CSWI1";
        CmsApdu response = client.getLogicalDeviceDirectory("C1", afterRef);
        //log.info("Response (ldName=C1, referenceAfter=CSWI1): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetLogicalDeviceDirectory asdu = (CmsGetLogicalDeviceDirectory) response.getAsdu();
        assertNotNull(asdu.lnReference());

        //log.info("Logical nodes under C1 after {}:", afterRef);
        //for (int i = 0; i < asdu.lnReference().size(); i++) {
        //    log.info("  [{}] {}", i, asdu.lnReference().get(i).get());
        //}

        // CSWI1 is at index 2, so we expect 4 entries: CSWI2, MMXU1, TVTR1
        assertEquals(totalCount - 3, asdu.lnReference().size());
        assertEquals("CSWI2", asdu.lnReference().get(0).get());
    }
}