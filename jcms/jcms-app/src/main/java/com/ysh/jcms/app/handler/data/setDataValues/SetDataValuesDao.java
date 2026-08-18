package com.ysh.jcms.app.handler.data.setDataValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.data.CmsDataRefValueEntry;
import com.ysh.jcms.core.pdu.data.CmsSetDataValuesRequest;
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
public class SetDataValuesDao extends BaseDao {

    /** Object references, e.g. "LD0/LLN0.Mod.stVal". */
    private List<String> references;

    /** Values to set, same order as references (already parsed by the CLI). */
    private List<CmsData> values;

    /** Optional FunctionalConstraint codes, same order as references. */
    private List<String> fcs;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(references, "references must not be null");
        Objects.requireNonNull(values, "values must not be null");
        if (fcs == null || fcs.isEmpty())
            fcs = new ArrayList<>(Collections.nCopies(references.size(), null));
        if (references.size() != values.size() || references.size() != fcs.size())
            throw new IllegalArgumentException("references values and fcs must have the same size");

        return new CmsSetDataValuesRequest()
            .data(
                IntStream.range(0, references.size())
                    .mapToObj(i -> new CmsDataRefValueEntry()
                        .reference(references.get(i))
                        .value(values.get(i))
                        .fc(fcs.get(i)))
                    .collect(Collectors.toList())
            );
    }
}
