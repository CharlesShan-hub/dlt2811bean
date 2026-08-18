package com.ysh.jcms.app.handler.data.getDataValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.data.CmsDataRefEntry;
import com.ysh.jcms.core.pdu.data.CmsGetDataValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Setter
@Getter
@Accessors(fluent = true)
public class GetDataValuesDao extends BaseDao {

    /** Data references, split and bound by {@link Param#convert(String)}. */
    private List<String> refs;

    /** Functional constraint filter, e.g. ST, MX. Default XX means no filter. */
    private List<String> fcs;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(refs, "refs must not be null");
        if (fcs == null || fcs.isEmpty())
            fcs = new ArrayList<>(Collections.nCopies(refs.size(), null));
        if (refs.size() != fcs.size())
            throw new IllegalArgumentException("refs and fcs must have the same size");

        return new CmsGetDataValuesRequest()
            .data(
                IntStream.range(0, refs.size())
                    .mapToObj(i -> new CmsDataRefEntry()
                        .reference(refs.get(i))
                        .fc(fcs.get(i)))
                    .collect(Collectors.toList())
            );
    }
}
