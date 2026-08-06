package com.ysh.jcms.app.handler.control.operate;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.bitarray.CmsCheck;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.pdu.control.CmsOperateRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Setter
@Getter
@Accessors(fluent = true)
public class OperateDao extends BaseDao {
    private String ref;
    private Map<String, String> args;

    @Override
    public CmsType toRequest() {
        CmsOperateRequest req = new CmsOperateRequest().reference(ref);

        String valueStr = args != null ? args.get("value") : null;
        if (valueStr != null && !valueStr.isEmpty()) {
            CmsData ctlVal = new CmsData();
            ctlVal.alt_boolean(Boolean.parseBoolean(valueStr));
            req.ctlVal(ctlVal);
        }

        String originStr = args != null ? args.get("origin") : null;
        if (originStr != null && !originStr.isEmpty()) {
            req.origin(new CmsOriginator().orCat(Integer.parseInt(originStr)));
        }

        String ctlNumStr = args != null ? args.get("ctlNum") : null;
        if (ctlNumStr != null && !ctlNumStr.isEmpty()) {
            req.ctlNum(Integer.parseInt(ctlNumStr));
        }

        String testStr = args != null ? args.get("test") : null;
        if (testStr != null)
            req.test(Boolean.parseBoolean(testStr));

        String checkStr = args != null ? args.get("check") : null;
        if (checkStr != null && !checkStr.isEmpty()) {
            CmsCheck check = new CmsCheck();
            for (String flag : checkStr.split(",")) {
                if ("syncheck".equalsIgnoreCase(flag.trim()))
                    check.syncheck(true);
                if ("interlock".equalsIgnoreCase(flag.trim()))
                    check.interlock_check(true);
            }
            req.check(check);
        }

        return req;
    }
}
