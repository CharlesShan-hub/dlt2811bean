package com.ysh.jcms.app.handler.control.operate;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.core.data.bitarray.CmsCheck;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.common.CmsOriginator;
import com.ysh.jcms.core.pdu.control.CmsOperateRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class OperateDao extends BaseDao {
    private String ref;
    private String value;
    private String origin;
    private String ctlNum;
    private String test;
    private String check;

    @Override
    public CmsType toRequest() {
        CmsOperateRequest req = new CmsOperateRequest().reference(ref);

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

        if (check != null && !check.isEmpty()) {
            CmsCheck ck = new CmsCheck();
            for (String flag : check.split(",")) {
                if ("syncheck".equalsIgnoreCase(flag.trim()))
                    ck.syncheck(true);
                if ("interlock".equalsIgnoreCase(flag.trim()))
                    ck.interlock_check(true);
            }
            req.check(ck);
        }

        return req;
    }
}
