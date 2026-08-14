package com.ysh.jcms.app.handler.sg.confirmEditSgValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.sg.CmsConfirmEditSgValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class ConfirmEditSgValuesDao extends BaseDao {
    private String sgcbReference;

    @Override
    public CmsType toRequest() {
        return new CmsConfirmEditSgValuesRequest().sgcbReference(sgcbReference);
    }
}
