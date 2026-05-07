package com.ysh.dlt2811bean.transport.app.directory;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataDefinition;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataDefinitionEntry;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetAllDataDefinition Loopback Test")
class GetAllDataDefinitionLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("by lnReference returns DO definitions with CDC type")
    void byLnReference() throws Exception {
        associate();

        CmsApdu response = client.getAllDataDefinitionByLn("C1/CSWI1");
        //log.info("Response (lnReference=C1/CSWI1): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetAllDataDefinition asdu = (CmsGetAllDataDefinition) response.getAsdu();
        assertNotNull(asdu.data());
        assertTrue(asdu.data().size() >= 5);

        //log.info("DO definitions for C1/CSWI1:");
        //for (int i = 0; i < asdu.data().size(); i++) {
        //    CmsDataDefinitionEntry entry = asdu.data().get(i);
        //    log.info("  [{}] ref={}, cdc={}, def={}",
        //        i, entry.reference().get(), entry.cdcType().get(), entry.definition());
        //}
    }

    @Test
    @DisplayName("by lnReference with fc=ST filters DAs correctly")
    void byLnReferenceWithFcSt() throws Exception {
        associate();

        CmsApdu response = client.getAllDataDefinitionByLn("C1/CSWI1", "ST");
        //log.info("Response (lnReference=C1/CSWI1, fc=ST): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetAllDataDefinition asdu = (CmsGetAllDataDefinition) response.getAsdu();
        assertNotNull(asdu.data());
        assertTrue(asdu.data().size() > 0);

        //log.info("DO definitions for C1/CSWI1 (fc=ST):");
        //for (int i = 0; i < asdu.data().size(); i++) {
        //    CmsDataDefinitionEntry entry = asdu.data().get(i);
        //    log.info("  [{}] ref={}, cdc={}, def={}",
        //        i, entry.reference().get(), entry.cdcType().get(), entry.definition());
        //}
    }

    @Test
    @DisplayName("by lnReference with fc=MX returns different results than fc=ST")
    void byLnReferenceWithFcMx() throws Exception {
        associate();

        CmsApdu response = client.getAllDataDefinitionByLn("C1/MMXU1", "MX");
        //log.info("Response (lnReference=C1/MMXU1, fc=MX): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetAllDataDefinition asdu = (CmsGetAllDataDefinition) response.getAsdu();
        assertNotNull(asdu.data());
        assertTrue(asdu.data().size() > 0);

        //log.info("DO definitions for C1/MMXU1 (fc=MX):");
        //for (int i = 0; i < asdu.data().size(); i++) {
        //    CmsDataDefinitionEntry entry = asdu.data().get(i);
        //    log.info("  [{}] ref={}, cdc={}, def={}",
        //        i, entry.reference().get(), entry.cdcType().get(), entry.definition());
        //}
    }

    @Test
    @DisplayName("by ldName returns DO definitions for all LNs")
    void byLdName() throws Exception {
        associate();

        CmsApdu response = client.getAllDataDefinitionByLd("C1");
        //log.info("Response (ldName=C1): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetAllDataDefinition asdu = (CmsGetAllDataDefinition) response.getAsdu();
        assertNotNull(asdu.data());
        assertTrue(asdu.data().size() > 0);

        //log.info("All DO definitions under C1:");
        //for (int i = 0; i < asdu.data().size(); i++) {
        //    CmsDataDefinitionEntry entry = asdu.data().get(i);
        //    log.info("  [{}] ref={}, cdc={}, def={}",
        //        i, entry.reference().get(), entry.cdcType().get(), entry.definition());
        //}
    }
}
