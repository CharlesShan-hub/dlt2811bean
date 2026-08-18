package com.ysh.jcms.app.handler.sg.getEditSgValue;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.sg.CmsSgRefFcEntry;
import com.ysh.jcms.core.pdu.sg.CmsGetEditSgValueRequest;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetEditSgValueDao extends BaseDao {

    /** Setting group references */
    private List<String> refs;

    /** Functional constraint, default SG */
    private String fc = "SG";

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(refs, "refs must not be null");
        return new CmsGetEditSgValueRequest()
            .data(
                refs.stream()
                    .map(ref -> new CmsSgRefFcEntry().reference(ref).fc(fc))
                    .collect(Collectors.toList())
            );
    }
}
