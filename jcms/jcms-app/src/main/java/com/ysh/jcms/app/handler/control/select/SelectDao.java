package com.ysh.jcms.app.handler.control.select;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.pdu.control.CmsSelectRequest;
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
