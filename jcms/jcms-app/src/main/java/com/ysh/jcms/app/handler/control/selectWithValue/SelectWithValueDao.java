package com.ysh.jcms.app.handler.control.selectWithValue;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.bitarray.CmsCheck;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.pdu.control.CmsSelectWithValueRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SelectWithValueDao extends BaseDao {
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
        CmsSelectWithValueRequest req = new CmsSelectWithValueRequest().reference(ref);

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

        if (test != null)
            req.test(Boolean.parseBoolean(test));

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
