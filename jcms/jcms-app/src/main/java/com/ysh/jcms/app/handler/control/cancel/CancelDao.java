package com.ysh.jcms.app.handler.control.cancel;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.pdu.control.CmsCancelRequest;
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

        if (test != null)
            req.test(Boolean.parseBoolean(test));

        return req;
    }
}