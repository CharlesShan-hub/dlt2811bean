package com.ysh.jcms.app.handler.control.cancel;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.control.CmsCancelRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class CancelDao extends BaseDao {

    /** Control object reference, format LD/LN.DO */
    private String ref;

    /** Control value (SPC), true/false */
    private Boolean ctlVal;

    /** Scheduled operate time (Unix seconds, optional) */
    private Long operTm;

    /** Originator category (orCat), 0=local 1=remote */
    private Integer origin;

    /** Command number */
    private Integer ctlNum;

    /** Timestamp (Unix seconds) */
    private Long t;

    /** Test flag */
    private Boolean test;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(ref, "ref must not be null");
        Objects.requireNonNull(ctlVal, "ctlVal must not be null");
        Objects.requireNonNull(origin, "origin must not be null");
        Objects.requireNonNull(ctlNum, "ctlNum must not be null");
        Objects.requireNonNull(t, "t must not be null");
        Objects.requireNonNull(test, "test must not be null");
        return new CmsCancelRequest()
            .reference(ref)
            .ctlVal(ctlVal)
            .operTm(operTm)
            .origin(origin)
            .ctlNum(ctlNum)
            .t(t)
            .test(test);
    }
}