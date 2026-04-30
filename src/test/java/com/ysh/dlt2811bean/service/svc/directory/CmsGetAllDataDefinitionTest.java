package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsFC;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataDefinitionEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetAllDataDefinition")
class CmsGetAllDataDefinitionTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetAllDataDefinition asdu = new CmsGetAllDataDefinition(MessageType.REQUEST)
            .ldName("IED1.AP1.LD1")
            .fc("ST")
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));
        CmsGetAllDataDefinition result = (CmsGetAllDataDefinition) decoded.getAsdu();

        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1", result.reference().ldName.get());
        assertTrue(result.isFieldPresent("fc"));
        assertEquals("ST", result.fc().get());
        assertFalse(result.isFieldPresent("referenceAfter"));
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetAllDataDefinition asdu = new CmsGetAllDataDefinition(MessageType.RESPONSE_POSITIVE).reqId(2);

        CmsDataDefinitionEntry entry = new CmsDataDefinitionEntry()
            .reference("DO1")
            .cdcType("BOOLEAN")
            .definition(CmsDataDefinition.ofBoolean());
        asdu.data().add(entry);

        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));
        CmsGetAllDataDefinition result = (CmsGetAllDataDefinition) decoded.getAsdu();

        assertEquals(2, result.reqId().get());
        assertEquals(1, result.data().size());
        assertEquals("DO1", result.data().get(0).reference().get());
        assertTrue(result.data().get(0).isFieldPresent("cdcType"));
        assertEquals("BOOLEAN", result.data().get(0).cdcType().get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetAllDataDefinition asdu = new CmsGetAllDataDefinition(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);
        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));
        CmsGetAllDataDefinition result = (CmsGetAllDataDefinition) decoded.getAsdu();

        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }
}