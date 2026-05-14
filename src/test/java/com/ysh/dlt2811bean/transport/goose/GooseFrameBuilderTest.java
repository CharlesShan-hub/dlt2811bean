package com.ysh.dlt2811bean.transport.goose;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GooseFrameBuilder Unit Test")
class GooseFrameBuilderTest {

    private final GooseFrameBuilder builder = new GooseFrameBuilder();

    @Test
    @DisplayName("buildGoosePayload produces non-empty payload")
    void buildGoosePayload() {
        GooseConfig config = GooseConfig.builder()
                .goCBRef("C1/LLN0.GoCB1")
                .goID("GOOSE001")
                .appId(0x0001)
                .dataSetRef("C1/LLN0.DS1")
                .build();

        GooseStateMachine state = new GooseStateMachine();
        state.onEvent();

        byte[] payload = builder.buildGoosePayload(config, state, null);

        assertNotNull(payload);
        assertTrue(payload.length > 3);
    }

    @Test
    @DisplayName("buildGoosePayload has correct APPID and service code")
    void payloadHeader() {
        GooseConfig config = GooseConfig.builder()
                .goCBRef("C1/LLN0.GoCB1")
                .goID("GOOSE001")
                .appId(0x1001)
                .dataSetRef("C1/LLN0.DS1")
                .build();

        GooseStateMachine state = new GooseStateMachine();
        state.onEvent();

        byte[] payload = builder.buildGoosePayload(config, state, null);

        assertEquals(0x10, payload[0] & 0xFF);
        assertEquals(0x01, payload[1] & 0xFF);
    }

    @Test
    @DisplayName("buildEthernetFrame produces valid ethernet frame")
    void buildEthernetFrame() {
        byte[] dstMac = new byte[]{0x01, 0x0C, (byte)0xCD, 0x01, 0x00, 0x01};
        GooseConfig config = GooseConfig.builder()
                .goCBRef("C1/LLN0.GoCB1")
                .goID("GOOSE001")
                .dstMac(dstMac)
                .appId(0x0001)
                .dataSetRef("C1/LLN0.DS1")
                .build();

        GooseStateMachine state = new GooseStateMachine();
        state.onEvent();

        byte[] frame = builder.buildEthernetFrame(config, state, null);

        assertNotNull(frame);
        assertTrue(frame.length > 14);

        for (int i = 0; i < 6; i++) {
            assertEquals(dstMac[i], frame[i]);
        }

        assertEquals(0x81, frame[12] & 0xFF);
        assertEquals(0x00, frame[13] & 0xFF);

        assertEquals(0x88, frame[16] & 0xFF);
        assertEquals(0xB8, frame[17] & 0xFF);
    }

    @Test
    @DisplayName("buildGoosePayload with different appIds produces different payloads")
    void differentAppIds() {
        GooseConfig config1 = GooseConfig.builder()
                .goCBRef("C1/LLN0.GoCB1")
                .goID("G1")
                .appId(0x0001)
                .dataSetRef("C1/LLN0.DS1")
                .build();

        GooseConfig config2 = GooseConfig.builder()
                .goCBRef("C1/LLN0.GoCB2")
                .goID("G2")
                .appId(0x0002)
                .dataSetRef("C1/LLN0.DS2")
                .build();

        GooseStateMachine state = new GooseStateMachine();
        state.onEvent();

        byte[] payload1 = builder.buildGoosePayload(config1, state, null);
        state.onEvent();
        byte[] payload2 = builder.buildGoosePayload(config2, state, null);

        assertNotEquals(payload1[1], payload2[1]);
    }
}
