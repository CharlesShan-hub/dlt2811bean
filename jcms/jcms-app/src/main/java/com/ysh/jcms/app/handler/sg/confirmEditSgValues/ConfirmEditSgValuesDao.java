package com.ysh.jcms.app.handler.sg.confirmEditSgValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.sg.CmsConfirmEditSgValuesRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class ConfirmEditSgValuesDao extends BaseDao {

    /** SGCB reference, e.g. "LD0/LLN0.SGCB" */
    private String sgcbReference;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(sgcbReference, "sgcbReference must not be null");
        return new CmsConfirmEditSgValuesRequest().sgcbReference(sgcbReference);
    }
}
