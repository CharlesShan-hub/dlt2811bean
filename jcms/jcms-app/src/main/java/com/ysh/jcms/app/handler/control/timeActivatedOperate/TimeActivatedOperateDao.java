package com.ysh.jcms.app.handler.control.timeActivatedOperate;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.bitarray.CmsCheck;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.common.CmsOriginator;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import com.ysh.jcms.core.pdu.control.CmsTimeActivatedOperateRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class TimeActivatedOperateDao extends BaseDao {
    private String ref;
    private String ctlVal;
    private String operTm;
    private String origin;
    private String ctlNum;
    private String t;
    private String test;
    private String check;

    @Override
    public CmsType toRequest() {
        CmsTimeActivatedOperateRequest req = new CmsTimeActivatedOperateRequest().reference(ref);

        if (operTm != null && !operTm.isEmpty()) {
            req.operTm(new CmsUtcTime().secondsSinceEpoch(Long.parseLong(operTm)));
        }

        if (t != null && !t.isEmpty()) {
            req.t(new CmsUtcTime().secondsSinceEpoch(Long.parseLong(t)));
        }

        if (ctlVal != null && !ctlVal.isEmpty()) {
            CmsData ctlValData = new CmsData();
            ctlValData.alt_boolean(Boolean.parseBoolean(ctlVal));
            req.ctlVal(ctlValData);
        }

        if (origin != null && !origin.isEmpty()) {
            req.origin(new CmsOriginator().orCat(Integer.parseInt(origin)));
        }

        if (ctlNum != null && !ctlNum.isEmpty()) {
            req.ctlNum(Integer.parseInt(ctlNum));
        }

        if (test != null && !test.isEmpty()) {
            req.test(Boolean.parseBoolean(test));
        }

        if (check != null && !check.isEmpty()) {
            CmsCheck checkObj = new CmsCheck();
            for (String flag : check.split(",")) {
                if ("syncheck".equalsIgnoreCase(flag.trim()))
                    checkObj.syncheck(true);
                if ("interlock".equalsIgnoreCase(flag.trim()))
                    checkObj.interlock_check(true);
            }
            req.check(checkObj);
        }

        return req;
    }
}
