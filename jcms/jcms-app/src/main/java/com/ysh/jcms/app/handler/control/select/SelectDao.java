package com.ysh.jcms.app.handler.control.select;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.control.CmsSelectRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SelectDao extends BaseDao {

    /** Control object reference, format LD/LN.DO */
    private String ref;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(ref, "ref must not be null");
        return new CmsSelectRequest().reference(ref);
    }
}
