package com.ysh.dlt2811bean.transport.goose;

import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.svc.goose.CmsSendGooseMessage;

import java.time.Instant;

public class GooseFrameBuilder {

    static final int GOOSE_ETH_TYPE = 0x88B8;

    public byte[] buildEthernetFrame(GooseConfig config, GooseStateMachine state, CmsData<?> dataValue) {
        byte[] goosePayload = buildGoosePayload(config, state, dataValue);
        byte[] srcMac = getLocalMac();

        int vlanTag = ((config.getVlanPriority() & 0x7) << 13)
                    | ((config.getVlanId() & 0xFFF) << 0);

        int totalLen = 14 + 4 + 2 + goosePayload.length;
        byte[] frame = new byte[totalLen];

        int off = 0;
        System.arraycopy(config.getDstMac(), 0, frame, off, 6);
        off += 6;
        System.arraycopy(srcMac, 0, frame, off, 6);
        off += 6;

        frame[off++] = (byte) 0x81;
        frame[off++] = (byte) 0x00;
        frame[off++] = (byte) (vlanTag >> 8);
        frame[off++] = (byte) (vlanTag);

        frame[off++] = (byte) (GOOSE_ETH_TYPE >> 8);
        frame[off++] = (byte) (GOOSE_ETH_TYPE);

        System.arraycopy(goosePayload, 0, frame, off, goosePayload.length);

        return frame;
    }

    public byte[] buildGoosePayload(GooseConfig config, GooseStateMachine state, CmsData<?> dataValue) {
        CmsSendGooseMessage gooseMsg = new CmsSendGooseMessage();
        gooseMsg.goID(config.getGoID());
        gooseMsg.datSet(String.join(",", config.getDataSetRefs()));
        gooseMsg.goRef(config.getGoCBRef());

        Instant now = Instant.now();
        gooseMsg.t.secondsSinceEpoch(now.getEpochSecond());
        int fraction = (int) ((now.getNano() / 1000L) * 16777215L / 1000000L);
        gooseMsg.t.fractionOfSecond(Math.min(fraction, 16777215));

        gooseMsg.stNum(state.getCurrentStNum());
        gooseMsg.sqNum(state.getCurrentSqNum());
        gooseMsg.simulation(false);
        gooseMsg.confRev(config.getConfRev());
        gooseMsg.ndsCom(false);

        if (dataValue != null) {
            gooseMsg.data = dataValue;
        } else {
            gooseMsg.data(new CmsBoolean(false));
        }

        PerOutputStream pos = new PerOutputStream();
        gooseMsg.encode(pos);
        byte[] asduBytes = pos.toByteArray();

        int appId = config.getAppId();
        int serviceCode = ServiceName.SEND_GOOSE_MESSAGE.getCode();

        int payloadLen = 2 + 1 + asduBytes.length;
        byte[] payload = new byte[payloadLen];

        payload[0] = (byte) (appId >> 8);
        payload[1] = (byte) (appId);
        payload[2] = (byte) serviceCode;
        System.arraycopy(asduBytes, 0, payload, 3, asduBytes.length);

        return payload;
    }

    private static byte[] getLocalMac() {
        try {
            var ni = java.net.NetworkInterface.getNetworkInterfaces();
            while (ni.hasMoreElements()) {
                var n = ni.nextElement();
                if (n.isLoopback() || !n.isUp()) continue;
                byte[] mac = n.getHardwareAddress();
                if (mac != null && mac.length == 6) return mac;
            }
        } catch (Exception e) {
        }
        return new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    }
}
