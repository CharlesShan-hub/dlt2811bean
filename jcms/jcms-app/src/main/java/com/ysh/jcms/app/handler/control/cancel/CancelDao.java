package com.ysh.jcms.app.handler.control.cancel;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.common.CmsOriginator;
import com.ysh.jcms.core.pdu.control.CmsCancelRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class CancelDao extends BaseDao {
    private String ref;
    private String value;
    private String origin;
    private String ctlNum;
    private String test;

    @Override
    public CmsType toRequest() {
        CmsCancelRequest req = new CmsCancelRequest().reference(ref);

        if (value != null && !value.isEmpty()) {
            CmsData ctlVal = new CmsData();
            ctlVal.alt_boolean(Boolean.parseBoolean(value));
            req.ctlVal(ctlVal);
        }

        if (origin != null && !origin.isEmpty()) {
            req.origin(new CmsOriginator().orCat(Integer.parseInt(origin)));
        }

        if (ctlNum != null && !ctlNum.isEmpty()) {
            req.ctlNum(Integer.parseInt(ctlNum));
        }

        if (test != null && !test.isEmpty())
            req.test(Boolean.parseBoolean(test));

        return req;
    }
}
