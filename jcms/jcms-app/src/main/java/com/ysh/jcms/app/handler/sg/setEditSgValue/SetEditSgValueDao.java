package com.ysh.jcms.app.handler.sg.setEditSgValue;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.sg.CmsSgRefValueEntry;
import com.ysh.jcms.core.pdu.sg.CmsSetEditSgValueRequest;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DAO for SetEditSGValue (8.6.3). Holds a list of (reference, value) tuples to
 * be sent to the server.
 */
@Setter
@Getter
@Accessors(fluent = true)
public class SetEditSgValueDao extends BaseDao {

    /** Setting group references */
    private List<String> refs;

    /** Values to set, same order as refs (parsed by CLI via CmsData.fromJson) */
    private List<CmsData> values;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(refs, "refs must not be null");
        Objects.requireNonNull(values, "values must not be null");
        if (refs.size() != values.size())
            throw new IllegalArgumentException("refs and values must have the same size");

        return new CmsSetEditSgValueRequest()
            .data(
                IntStream.range(0, refs.size())
                    .mapToObj(i -> new CmsSgRefValueEntry()
                        .reference(refs.get(i))
                        .value(values.get(i)))
                    .collect(Collectors.toList())
            );
    }
}
