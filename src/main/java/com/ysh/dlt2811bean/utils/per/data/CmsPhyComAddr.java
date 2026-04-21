package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;

import static com.ysh.dlt2811bean.utils.per.data.CmsOctetString.Mode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 physical communication address type (§7.3.12, Table 13).
 *
 * <pre>
 * ┌──────────┬───────────────────┬────────────────────┬───────────┐
 * │ Field    │ 2811 Type         │ Constraints        │ Java type │
 * ├──────────┼───────────────────┼────────────────────┼───────────┤
 * │ addr     │ OCTET STRING      │ SIZE(6)            │ byte[6]   │
 * │ priority │ INT8U             │ —                  │ int       │
 * │ vid      │ INT16U            │ —                  │ int       │
 * │ appid    │ INT16U            │ —                  │ int       │
 * └──────────┴───────────────────┴────────────────────┴───────────┘
 * </pre>
 *
 * <pre>
 * // Create
 * CmsPhyComAddr addr = new CmsPhyComAddr();
 * addr.setAddr(new byte[6])
 *      .setPriority(4)
 *      .setVid(100)
 *      .setAppid(0x0001);
 *
 * // Encode / Decode
 * CmsPhyComAddr.encode(pos, addr);
 * CmsPhyComAddr r = CmsPhyComAddr.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsPhyComAddr {

    private byte[] addr;
    private int priority;
    private int vid;
    private int appid;

    public CmsPhyComAddr() {}

    // ==================== Encode / Decode ====================

    public static void encode(PerOutputStream pos, CmsPhyComAddr value) {
        CmsOctetString.encode(pos, value.addr, Mode.FIXED, 6);
        PerInteger.encode(pos, value.priority & 0xFFL, 0, 255);
        PerInteger.encode(pos, value.vid & 0xFFFFL, 0, 65535);
        PerInteger.encode(pos, value.appid & 0xFFFFL, 0, 65535);
    }

    public static CmsPhyComAddr decode(PerInputStream pis) throws PerDecodeException {
        CmsPhyComAddr addr = new CmsPhyComAddr();
        addr.addr = CmsOctetString.decode(pis, Mode.FIXED, 6).getValue();
        addr.priority = (int) PerInteger.decode(pis, 0, 255);
        addr.vid = (int) PerInteger.decode(pis, 0, 65535);
        addr.appid = (int) PerInteger.decode(pis, 0, 65535);
        return addr;
    }

    @Override
    public String toString() {
        return String.format("PhyComAddr[pri=%d, vid=%d, appid=0x%04X]",
                priority, vid, appid);
    }
}
