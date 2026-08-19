package com.ysh.jcms.app.handler.control.operate;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.control.CmsOperateRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class OperateDao extends BaseDao {

    /** Control object reference, format LD/LN.DO */
    private String ref;

    /** Control value (SPC), true/false */
    private Boolean ctlVal;

    /** Originator category (orCat), 0=local 1=remote */
    private Integer origin;

    /** Command number */
    private Integer ctlNum;

    /** Timestamp (Unix seconds) */
    private Long t;

    /** Test flag */
    private Boolean test;

    /** Check bitmap (0=none, 1=syncheck, 2=interlock, 3=both) */
    private Integer check;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(ref, "ref must not be null");
        return new CmsOperateRequest()
            .reference(ref)
            .ctlVal(ctlVal)
            .origin(origin)
            .ctlNum(ctlNum)
            .t(t)
            .test(test)
            .check(check);
    }
}
