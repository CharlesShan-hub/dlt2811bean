package com.ysh.dlt2811bean.datatypes.compound;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt8U;
import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
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
 * // Chain usage
 * CmsPhyComAddr addr = new CmsPhyComAddr()
 *     .addr(mac)
 *     .priority(4)
 *     .vid(100)
 *     .appid(0x0001);
 *
 * // Quick mode
 * CmsPhyComAddr addr = new CmsPhyComAddr(new byte[6], 4, 100, 0x0001);
 *
 * // Encode / Decode
 * addr.encode(pos);
 * CmsPhyComAddr r = new CmsPhyComAddr().decode(pis);
 * </pre>
 */
@Setter
@Accessors(fluent = true)
public class CmsPhyComAddr extends AbstractCmsCompound<CmsPhyComAddr> {

    public static final int ADDR_SIZE = 6;

    public CmsOctetString addr = new CmsOctetString().size(ADDR_SIZE);
    public CmsInt8U priority = new CmsInt8U();
    public CmsInt16U vid = new CmsInt16U();
    public CmsInt16U appid = new CmsInt16U();

    public CmsPhyComAddr() {
        super("PhyComAddr");
        registerField("addr");
        registerField("priority");
        registerField("vid");
        registerField("appid");
    }

    public CmsPhyComAddr(byte[] addr, int priority, int vid, int appid) {
        this();
        this.addr.set(addr);
        this.priority.set(priority);
        this.vid.set(vid);
        this.appid.set(appid);
    }

    // ==================== Convenience Setters ====================

    public CmsPhyComAddr addr(byte[] value) {
        this.addr.set(value);
        return this;
    }

    public CmsPhyComAddr priority(int value) {
        this.priority.set(value);
        return this;
    }

    public CmsPhyComAddr vid(int value) {
        this.vid.set(value);
        return this;
    }

    public CmsPhyComAddr appid(int value) {
        this.appid.set(value);
        return this;
    }
}