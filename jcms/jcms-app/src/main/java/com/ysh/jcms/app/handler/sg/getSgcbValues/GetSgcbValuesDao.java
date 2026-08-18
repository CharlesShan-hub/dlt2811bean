package com.ysh.jcms.app.handler.sg.getSgcbValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.pdu.sg.CmsGetSgcbValuesRequest;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetSgcbValuesDao extends BaseDao {

    /** SGCB references, e.g. "LD0/LLN0.SGCB" */
    private List<String> refs;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(refs, "refs must not be null");
        return new CmsGetSgcbValuesRequest()
            .sgcbReference(
                refs.stream()
                    .map(CmsObjectReference::new)
                    .collect(Collectors.toList())
            );
    }
}
