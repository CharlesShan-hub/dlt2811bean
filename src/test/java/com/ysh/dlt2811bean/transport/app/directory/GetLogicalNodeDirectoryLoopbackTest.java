package com.ysh.dlt2811bean.transport.app.directory;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalNodeDirectory;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetLogicalNodeDirectory Loopback Test")
class GetLogicalNodeDirectoryLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("GetLogicalNodeDirectory by ldName returns DOs from all LNs")
    void byLdName() throws Exception {
        associate();

        CmsApdu response = client.getLogicalNodeDirectoryByLd("C1");
        log.info("Response (ldName=C1): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetLogicalNodeDirectory asdu = (CmsGetLogicalNodeDirectory) response.getAsdu();
        assertNotNull(asdu.referenceResponse());
        assertTrue(asdu.referenceResponse().size() > 0);

        log.info("Data objects under C1:");
        for (int i = 0; i < asdu.referenceResponse().size(); i++) {
            log.info("  [{}] {}", i, asdu.referenceResponse().get(i).get());
        }
    }

    @Test
    @DisplayName("GetLogicalNodeDirectory by lnReference returns DOs from specific LN")
    void byLnReference() throws Exception {
        associate();

        CmsApdu response = client.getLogicalNodeDirectoryByLn("C1/CSWI1");
        log.info("Response (lnReference=C1/CSWI1): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetLogicalNodeDirectory asdu = (CmsGetLogicalNodeDirectory) response.getAsdu();
        assertNotNull(asdu.referenceResponse());
        assertTrue(asdu.referenceResponse().size() > 0);

        log.info("Data objects under C1/CSWI1:");
        for (int i = 0; i < asdu.referenceResponse().size(); i++) {
            log.info("  [{}] {}", i, asdu.referenceResponse().get(i).get());
        }
    }

    @Test
    @DisplayName("GetLogicalNodeDirectory by lnReference with referenceAfter returns entries after that position")
    void byLnReferenceWithReferenceAfter() throws Exception {
        associate();

        CmsApdu allResponse = client.getLogicalNodeDirectoryByLn("C1/CSWI1");
        CmsGetLogicalNodeDirectory allAsdu = (CmsGetLogicalNodeDirectory) allResponse.getAsdu();
        int totalCount = allAsdu.referenceResponse().size();
        log.info("Total DOs under C1/CSWI1: {}", totalCount);

        // Expected order (from CSWIa LNodeType): Mod, Health, Beh, Pos, GrpAl
        CmsApdu response = client.getLogicalNodeDirectoryByLn("C1/CSWI1", "Beh");
        log.info("Response (lnReference=C1/CSWI1, referenceAfter=Beh): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetLogicalNodeDirectory asdu = (CmsGetLogicalNodeDirectory) response.getAsdu();
        assertNotNull(asdu.referenceResponse());

        log.info("Data objects under C1/CSWI1 after Beh:");
        for (int i = 0; i < asdu.referenceResponse().size(); i++) {
            log.info("  [{}] {}", i, asdu.referenceResponse().get(i).get());
        }

        // Beh is at index 2, expected remaining: Pos, GrpAl
        assertEquals(totalCount - 3, asdu.referenceResponse().size());
        assertEquals("Pos", asdu.referenceResponse().get(0).get());
    }
}