package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerPhyComAddr;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt8U;

/**
 * PhyComAddr ::= SEQUENCE { addr, priority, vid, appid } — 7.3.12
 */
public class CmsPhyComAddr extends CmsType {

    public byte[] addr = new byte[6];
    public CmsInt8U priority = new CmsInt8U();
    public CmsInt16U vid = new CmsInt16U();
    public CmsInt16U appid = new CmsInt16U();

    public CmsPhyComAddr() {
        super(new InnerPhyComAddr());
    }

    public CmsPhyComAddr addr(byte[] v) { this.addr = v; return this; }
    public CmsPhyComAddr priority(int v) { this.priority.value(v); return this; }
    public CmsPhyComAddr vid(int v) { this.vid.value(v); return this; }
    public CmsPhyComAddr appid(int v) { this.appid.value(v); return this; }

    @Override
    public void syncToInner() {
        InnerPhyComAddr i = (InnerPhyComAddr) inner;
        i.addr.value = addr;
        i.priority.value = priority.value();
        i.vid.value = vid.value();
        i.appid.value = appid.value();
    }

    @Override
    public void syncFromInner() {
        InnerPhyComAddr i = (InnerPhyComAddr) inner;
        addr = i.addr.value;
        priority.value(i.priority.value);
        vid.value(i.vid.value);
        appid.value(i.appid.value);
    }
}
