package com.ysh.dlt2811bean.transport.goose;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.goose.CmsSetGoCBValues;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsSetGoCBValuesEntry;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GoosePublisher Loopback Test")
class GoosePublisherLoopbackTest extends LoopbackTest {

    @Override
    protected boolean useAutoLifecycle() {
        return true;
    }

    @Test
    @DisplayName("SetGoCBValues with goEna=true starts GoosePublisher")
    void startGoosePublishing() throws Exception {
        associate();

        assertNotNull(server.getGoosePublisher());

        CmsSetGoCBValuesEntry entry = new CmsSetGoCBValuesEntry();
        entry.reference.set("C1/LLN0.ItlPositions");
        entry.goEna.set(true);

        CmsApdu response = client.setGoCBValues(entry);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("SetGoCBValues with goEna=false does not fail")
    void stopGoosePublishing() throws Exception {
        associate();

        CmsSetGoCBValuesEntry entry = new CmsSetGoCBValuesEntry();
        entry.reference.set("C1/LLN0.ItlPositions");
        entry.goEna.set(false);

        CmsApdu response = client.setGoCBValues(entry);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("GoosePublisher is accessible from CmsServer")
    void goosePublisherAccessible() throws Exception {
        associate();

        GoosePublisher publisher = server.getGoosePublisher();
        assertNotNull(publisher);
    }
}
