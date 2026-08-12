package com.ysh.jcms.app.handler.test.test;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.core.data.core.CmsNull;
import com.ysh.jcms.core.data.core.CmsType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class TestDao extends BaseDao {

    @Override
    public CmsType toRequest() {
        return new CmsNull();
    }
}
