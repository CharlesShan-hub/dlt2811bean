package com.ysh.jcms.app.handler.goose.getGooseElementNumber;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.goose.CmsGoRefFcEntry;
import com.ysh.jcms.core.pdu.goose.CmsGetGooseElementNumberRequest;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetGooseElementNumberDao extends BaseDao {

    /** GOOSE control block reference, e.g. "LD0/LLN0.gocb1" */
    private String gocbReference;

    /** Member references, e.g. "LD0/LLN0.DO1" */
    private List<String> memberRefs;

    /** Member FC codes (numeric), same order as memberRefs */
    private List<String> memberFcs;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(gocbReference, "gocbReference must not be null");
        Objects.requireNonNull(memberRefs, "memberRefs must not be null");
        Objects.requireNonNull(memberFcs, "memberFcs must not be null");
        if (memberRefs.size() != memberFcs.size())
            throw new IllegalArgumentException("memberRefs and memberFcs must have the same size");

        return new CmsGetGooseElementNumberRequest()
            .gocbReference(gocbReference)
            .memberData(
                IntStream.range(0, memberRefs.size())
                    .mapToObj(i -> new CmsGoRefFcEntry()
                        .reference(memberRefs.get(i))
                        .fc(memberFcs.get(i)))
                    .collect(Collectors.toList())
            );
    }
}
