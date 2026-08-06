package com.ysh.jcms.app.handler.sg.confirmEditSgValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.pdu.sg.CmsConfirmEditSgValuesRequest;
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
