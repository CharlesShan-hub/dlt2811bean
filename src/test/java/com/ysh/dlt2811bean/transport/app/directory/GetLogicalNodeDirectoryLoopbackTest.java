package com.ysh.dlt2811bean.transport.app.directory;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalNodeDirectory;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetLogicalNodeDirectory Loopback Test")
class GetLogicalNodeDirectoryLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("DATA_OBJECT: by ldName returns DOs from all LNs")
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
    @DisplayName("DATA_OBJECT: by lnReference returns DOs from specific LN")
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
    @DisplayName("DATA_OBJECT: by lnReference with referenceAfter returns entries after that position")
    void byLnReferenceWithReferenceAfter() throws Exception {
        associate();

        CmsApdu allResponse = client.getLogicalNodeDirectoryByLn("C1/CSWI1");
        CmsGetLogicalNodeDirectory allAsdu = (CmsGetLogicalNodeDirectory) allResponse.getAsdu();
        int totalCount = allAsdu.referenceResponse().size();
        log.info("Total DOs under C1/CSWI1: {}", totalCount);

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

        assertEquals(totalCount - 3, asdu.referenceResponse().size());
        assertEquals("Pos", asdu.referenceResponse().get(0).get());
    }

    @Test
    @DisplayName("DATA_SET: returns dataset names from LN0")
    void dataSet() throws Exception {
        associate();

        CmsApdu response = client.getLogicalNodeDirectoryByLd("C1", CmsACSIClass.DATA_SET);
        log.info("Response (DATA_SET): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetLogicalNodeDirectory asdu = (CmsGetLogicalNodeDirectory) response.getAsdu();
        assertNotNull(asdu.referenceResponse());
        assertEquals(3, asdu.referenceResponse().size());

        log.info("DataSets under C1:");
        for (int i = 0; i < asdu.referenceResponse().size(); i++) {
            log.info("  [{}] {}", i, asdu.referenceResponse().get(i).get());
        }

        assertEquals("Positions", asdu.referenceResponse().get(0).get());
        assertEquals("Measurands", asdu.referenceResponse().get(1).get());
        assertEquals("smv", asdu.referenceResponse().get(2).get());
    }

    @Test
    @DisplayName("BRCB: no buffered report controls in this SCD")
    void brcb() throws Exception {
        associate();

        CmsApdu response = client.getLogicalNodeDirectoryByLd("C1", CmsACSIClass.BRCB);
        log.info("Response (BRCB): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetLogicalNodeDirectory asdu = (CmsGetLogicalNodeDirectory) response.getAsdu();
        assertTrue(asdu.referenceResponse().isEmpty());
    }

    @Test
    @DisplayName("URCB: returns unbuffered report control names")
    void urcb() throws Exception {
        associate();

        CmsApdu response = client.getLogicalNodeDirectoryByLd("C1", CmsACSIClass.URCB);
        log.info("Response (URCB): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetLogicalNodeDirectory asdu = (CmsGetLogicalNodeDirectory) response.getAsdu();
        assertNotNull(asdu.referenceResponse());
        assertEquals(2, asdu.referenceResponse().size());

        log.info("Unbuffered reports under C1:");
        for (int i = 0; i < asdu.referenceResponse().size(); i++) {
            log.info("  [{}] {}", i, asdu.referenceResponse().get(i).get());
        }

        assertEquals("PosReport", asdu.referenceResponse().get(0).get());
        assertEquals("MeaReport", asdu.referenceResponse().get(1).get());
    }

    @Test
    @DisplayName("LCB: returns log control names")
    void lcb() throws Exception {
        associate();

        CmsApdu response = client.getLogicalNodeDirectoryByLd("C1", CmsACSIClass.LCB);
        log.info("Response (LCB): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetLogicalNodeDirectory asdu = (CmsGetLogicalNodeDirectory) response.getAsdu();
        assertNotNull(asdu.referenceResponse());
        assertEquals(1, asdu.referenceResponse().size());

        log.info("Log controls under C1:");
        for (int i = 0; i < asdu.referenceResponse().size(); i++) {
            log.info("  [{}] {}", i, asdu.referenceResponse().get(i).get());
        }

        assertEquals("Log", asdu.referenceResponse().get(0).get());
    }

    @Test
    @DisplayName("LOG: returns log names from log controls")
    void log() throws Exception {
        associate();

        CmsApdu response = client.getLogicalNodeDirectoryByLd("C1", CmsACSIClass.LOG);
        log.info("Response (LOG): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetLogicalNodeDirectory asdu = (CmsGetLogicalNodeDirectory) response.getAsdu();
        assertNotNull(asdu.referenceResponse());
        assertEquals(1, asdu.referenceResponse().size());

        assertEquals("C1", asdu.referenceResponse().get(0).get());
    }

    @Test
    @DisplayName("GO_CB: returns GOOSE control names")
    void goCb() throws Exception {
        associate();

        CmsApdu response = client.getLogicalNodeDirectoryByLd("C1", CmsACSIClass.GO_CB);
        log.info("Response (GO_CB): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetLogicalNodeDirectory asdu = (CmsGetLogicalNodeDirectory) response.getAsdu();
        assertNotNull(asdu.referenceResponse());
        assertEquals(1, asdu.referenceResponse().size());

        assertEquals("ItlPositions", asdu.referenceResponse().get(0).get());
    }

    @Test
    @DisplayName("MSV_CB: returns sampled value control names")
    void msvCb() throws Exception {
        associate();

        CmsApdu response = client.getLogicalNodeDirectoryByLd("C1", CmsACSIClass.MSV_CB);
        log.info("Response (MSV_CB): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetLogicalNodeDirectory asdu = (CmsGetLogicalNodeDirectory) response.getAsdu();
        assertNotNull(asdu.referenceResponse());
        assertEquals(1, asdu.referenceResponse().size());

        assertEquals("Volt", asdu.referenceResponse().get(0).get());
    }
}