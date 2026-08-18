package com.ysh.jcms.app.handler.goose.setGoCbValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.goose.CmsSetGoCbEntry;
import com.ysh.jcms.core.pdu.goose.CmsSetGoCbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Setter
@Getter
@Accessors(fluent = true)
public class SetGoCbValuesDao extends BaseDao {
    /** GOOSE control block references, e.g. "LD0/LLN0.GoCB1" */
    private List<String> refs;
    
    /** GOOSE enable flags, same order as refs */
    private List<Boolean> goEnas;
    
    /** GOOSE IDs, same order as refs */
    private List<String> goIDs;
    
    /** Dataset references, same order as refs */
    private List<String> datSets;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(refs, "refs must not be null");
        Objects.requireNonNull(goEnas, "goEnas must not be null");
        Objects.requireNonNull(goIDs, "goIDs must not be null");
        Objects.requireNonNull(datSets, "datSets must not be null");
        if (refs.size() != goEnas.size() || refs.size() != goIDs.size() || refs.size() != datSets.size())
            throw new IllegalArgumentException("refs, goEnas, goIDs, and datSets must have the same size");

        return new CmsSetGoCbValuesRequest()
            .gocb(
                IntStream.range(0, refs.size())
                    .mapToObj(i -> new CmsSetGoCbEntry()
                        .reference(refs.get(i))
                        .goEna(goEnas.get(i))
                        .goID(goIDs.get(i))
                        .datSet(datSets.get(i)))
                    .collect(Collectors.toList())
            );
    }
}
