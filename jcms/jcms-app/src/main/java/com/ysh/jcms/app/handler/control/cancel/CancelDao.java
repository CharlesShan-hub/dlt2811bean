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

    /** Originator category (orCat), 0=local 1=remote */
    private Integer origin;

    /** Command number */
    private Integer ctlNum;

    /** Test flag */
    private Boolean test;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(ref, "ref must not be null");
        return new CmsCancelRequest()
            .reference(ref)
            .ctlVal(ctlVal)
            .origin(origin)
            .ctlNum(ctlNum)
            .test(test);
    }
}
