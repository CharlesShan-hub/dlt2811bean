package com.ysh.jcms.data.time;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import java.util.Arrays;
import java.util.List;

/**
 * BinaryTime ::= OCTET STRING (SIZE(6))  —  7.2.2
 * PER: 6 bytes aligned (fixed OCTET STRING)
 *
 * Byte layout: [0..3] msOfDay, [4..5] daysSince1984
 *
 * All-pointer container:
 *   [0] msOfDay        → CmsInt32U*
 *   [8] daysSince1984  → CmsInt16U*
 */
public class CmsBinaryTime extends CmsType {

    public CmsInt32U msOfDay;
    public CmsInt16U daysSince1984;

    public CmsBinaryTime() {
        this.msOfDay = new CmsInt32U();
        this.daysSince1984 = new CmsInt16U();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(msOfDay, daysSince1984);
    }
}
