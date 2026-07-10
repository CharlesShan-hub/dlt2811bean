package com.ysh.jcms.data.common;

import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * PhyComAddr ::= SEQUENCE { addr, priority, vid, appid } — 7.3.12
 */
public class CmsPhyComAddr extends CmsType {

    public CmsUint8Array addr;
    public CmsInt8U priority;
    public CmsInt16U vid;
    public CmsInt16U appid;

    public CmsPhyComAddr() {
        super(Codec.PHY_COM_ADDR);
        this.addr = new CmsUint8Array();
        this.priority = new CmsInt8U();
        this.vid = new CmsInt16U();
        this.appid = new CmsInt16U();
    }

    public CmsPhyComAddr addr(byte[] v) {
        this.addr.value(v);
        return this;
    }
    public CmsPhyComAddr priority(int v) {
        this.priority.value(v);
        return this;
    }
    public CmsPhyComAddr vid(int v) {
        this.vid.value(v);
        return this;
    }
    public CmsPhyComAddr appid(int v) {
        this.appid.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(addr, priority, vid, appid);
    }
}
