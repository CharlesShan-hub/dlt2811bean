package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import static com.ysh.dlt2811bean.utils.per.data2.CmsOctetString.Mode;
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
        CmsInt8U.encode(pos, value.priority);
        CmsInt16U.encode(pos, value.vid);
        CmsInt16U.encode(pos, value.appid);
    }

    public static CmsPhyComAddr decode(PerInputStream pis) throws PerDecodeException {
        CmsPhyComAddr addr = new CmsPhyComAddr();
        addr.addr = CmsOctetString.decode(pis, Mode.FIXED, 6).getValue();
        addr.priority = CmsInt8U.decode(pis).getValue();
        addr.vid = CmsInt16U.decode(pis).getValue();
        addr.appid = CmsInt16U.decode(pis).getValue();
        return addr;
    }

    @Override
    public String toString() {
        return String.format("PhyComAddr[pri=%d, vid=%d, appid=0x%04X]",
                priority, vid, appid);
    }
}
