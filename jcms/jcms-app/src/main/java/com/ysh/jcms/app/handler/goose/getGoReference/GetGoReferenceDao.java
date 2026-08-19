package com.ysh.jcms.app.handler.goose.getGoReference;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.scalar.CmsInt16U;
import com.ysh.jcms.core.pdu.goose.CmsGetGoReferenceRequest;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetGoReferenceDao extends BaseDao {

    /** GOOSE control block reference, e.g. "LD0/LLN0.gocb1" */
    private String gocbReference;

    /** Member offsets, e.g. "0 1 2" */
    private List<String> memberOffsets;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(gocbReference, "gocbReference must not be null");
        return new CmsGetGoReferenceRequest()
            .gocbReference(gocbReference)
            .memberOfs(
                memberOffsets == null ? java.util.Collections.emptyList() : memberOffsets.stream()
                    .map(Integer::parseInt)
                    .map(CmsInt16U::new)
                    .collect(Collectors.toList())
            );
    }
}
