package com.ysh.jcms.app.handler.control.select;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.control.CmsSelectRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SelectDao extends BaseDao {
    private String ref;

    @Override
    public CmsType toRequest() {
        return new CmsSelectRequest().reference(ref);
    }
}
