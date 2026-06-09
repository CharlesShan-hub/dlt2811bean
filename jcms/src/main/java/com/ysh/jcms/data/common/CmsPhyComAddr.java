package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * PhyComAddr ::= SEQUENCE {
 *     addr     [0] OCTET STRING (SIZE(6)),
 *     priority [1] Int8U,
 *     vid      [2] Int16U,
 *     appid    [3] Int16U
 * }  —  7.3.12
 *
 * All-pointer container:
 *   [0]  addr      → CmsUint8Array*
 *   [8]  priority  → CmsInt8U*
 *   [16] vid       → CmsInt16U*
 *   [24] appid     → CmsInt16U*
 */
public class CmsPhyComAddr extends CmsType {

    public CmsUint8Array addr;
    public CmsInt8U      priority;
    public CmsInt16U     vid;
    public CmsInt16U     appid;

    public CmsPhyComAddr() {
        this.addr     = new CmsUint8Array();
        this.priority = new CmsInt8U();
        this.vid      = new CmsInt16U();
        this.appid    = new CmsInt16U();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(addr, priority, vid, appid);
    }
}
