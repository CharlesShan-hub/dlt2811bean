package com.ysh.dlt2811bean.transport.goose;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder
public class GooseConfig {

    private final String goCBRef;
    private final String goID;
    private final byte[] dstMac;
    private final int appId;
    private final int vlanPriority;
    private final int vlanId;
    private final long confRev;
    @Singular
    private final List<String> dataSetRefs;
    private final long steadyRetransmitMs;

    public static class GooseConfigBuilder {
        private byte[] dstMac = new byte[]{0x01, 0x0C, (byte)0xCD, 0x01, 0x00, 0x01};
        private int appId = 0x0001;
        private int vlanPriority = 4;
        private int vlanId = 0;
        private long confRev = 1;
        private long steadyRetransmitMs = 5000;
    }
}
