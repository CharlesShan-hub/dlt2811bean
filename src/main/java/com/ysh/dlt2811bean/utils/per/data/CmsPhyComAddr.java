package com.ysh.dlt2811bean.utils.per.data;

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
 *     .addr(new CmsOctetString().size(6).set(mac))
 *     .priority(new CmsInt8U().set(4))
 *     .vid(new CmsInt16U().set(100))
 *     .appid(new CmsInt16U().set(0x0001));
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

    public CmsOctetString addr = new CmsOctetString().size(6);
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
}