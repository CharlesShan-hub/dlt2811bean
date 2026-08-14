package com.ysh.jcms.app.handler.sg.selectEditSg;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.sg.CmsSelectEditSgRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SelectEditSgDao extends BaseDao {
    private String sgcbReference;
    private int settingGroupNumber;

    @Override
    public CmsType toRequest() {
        return new CmsSelectEditSgRequest().sgcbReference(sgcbReference).settingGroupNumber(settingGroupNumber);
    }
}
