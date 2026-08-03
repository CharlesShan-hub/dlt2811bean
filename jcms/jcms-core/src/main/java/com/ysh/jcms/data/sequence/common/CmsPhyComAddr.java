package com.ysh.jcms.data.sequence.common;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerPhyComAddr;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.data.scalar.CmsOctetString;

/**
 * <pre>
 * {@code
 * PhyComAddr ::= SEQUENCE {
 *     addr        [0] IMPLICIT OCTET STRING (SIZE(6)),
 *     priority    [1] IMPLICIT Int8U,
 *     vid         [2] IMPLICIT Int16U,
 *     appid       [3] IMPLICIT Int16U
 * } — 7.3.12
 * }
 * </pre>
 */
public class CmsPhyComAddr extends CmsSequence {
    @CmsField
    public CmsOctetString addr;
    @CmsField
    public CmsInt8U priority;
    @CmsField
    public CmsInt16U vid;
    @CmsField
    public CmsInt16U appid;

    public CmsPhyComAddr() {
        super(new InnerPhyComAddr());
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
    public CmsPhyComAddr value(CmsPhyComAddr v) {
        return addr(v.addr.value()).priority(v.priority.value()).vid(v.vid.value()).appid(v.appid.value());
    }
}
