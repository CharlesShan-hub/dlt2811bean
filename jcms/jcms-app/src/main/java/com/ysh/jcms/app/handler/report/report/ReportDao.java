package com.ysh.jcms.app.handler.report.report;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class ReportDao extends BaseDao {

    @Override
    public CmsType toRequest() {
        return null;
    }
}
