package com.ysh.jcms.app.handler.sg.selectEditSg;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.sg.CmsSelectEditSgRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SelectEditSgDao extends BaseDao {

    /** SGCB reference, e.g. "LD0/LLN0.SGCB" */
    private String sgcbReference;

    /** Setting group number to edit */
    private int settingGroupNumber;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(sgcbReference, "sgcbReference must not be null");
        return new CmsSelectEditSgRequest()
            .sgcbReference(sgcbReference)
            .settingGroupNumber(settingGroupNumber);
    }
}
