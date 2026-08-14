package com.ysh.jcms.app.handler.sg.selectActiveSg;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.sg.CmsSelectActiveSgRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SelectActiveSgDao extends BaseDao {
    private String sgcbReference;
    private int settingGroupNumber;

    @Override
    public CmsType toRequest() {
        return new CmsSelectActiveSgRequest().sgcbReference(sgcbReference).settingGroupNumber(settingGroupNumber);
    }
}
