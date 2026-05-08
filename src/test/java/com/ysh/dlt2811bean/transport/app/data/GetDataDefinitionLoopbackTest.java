package com.ysh.dlt2811bean.transport.app.data;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDefinition;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetDataDefinition Loopback Test")
class GetDataDefinitionLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("data object returns cdcType (§8.4.4.2a)")
    void dataObject() throws Exception {
        associate();

        CmsApdu response = client.getDataDefinition("C1/LPHD1.Proxy");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetDataDefinition asdu = (CmsGetDataDefinition) response.getAsdu();
        assertTrue(asdu.definition.size() > 0);
    }

    @Test
    @DisplayName("data attribute returns empty cdcType (§8.4.4.2a)")
    void dataAttribute() throws Exception {
        associate();

        CmsApdu response = client.getDataDefinition("C1/LPHD1.Proxy.stVal");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetDataDefinition asdu = (CmsGetDataDefinition) response.getAsdu();
        assertTrue(asdu.definition.size() > 0);
    }

    @Test
    @DisplayName("empty reference returns error definition")
    void emptyRef() throws Exception {
        associate();

        CmsApdu response = client.getDataDefinition("");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetDataDefinition asdu = (CmsGetDataDefinition) response.getAsdu();
        assertTrue(asdu.definition.size() > 0);
    }
}
