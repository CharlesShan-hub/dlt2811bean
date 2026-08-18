package com.ysh.jcms.app.handler.dataset.createDataSet;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.core.pdu.dataset.CmsCreateDataSetRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Setter
@Getter
@Accessors(fluent = true)
public class CreateDataSetDao extends BaseDao {

    /** Data set reference, e.g. "LD0/LLN0.dsData" */
    private String datasetReference;

    /** Optional pagination: add members after this reference */
    private String referenceAfter;

    /** Member references, e.g. "LD0/LLN0.Beh" */
    private List<String> memberRefs;

    /** Member FC codes, same order as memberRefs (e.g. "ST", "MX") */
    private List<String> memberFcs;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(datasetReference, "datasetReference must not be null");
        Objects.requireNonNull(memberRefs, "memberRefs must not be null");
        Objects.requireNonNull(memberFcs, "memberFcs must not be null");
        if (memberRefs.size() != memberFcs.size())
            throw new IllegalArgumentException("memberRefs and memberFcs must have the same size");

        return new CmsCreateDataSetRequest()
            .datasetReference(datasetReference)
            .referenceAfter(referenceAfter)
            .memberData(
                IntStream.range(0, memberRefs.size())
                    .mapToObj(i -> new CmsDataRefFcEntry()
                        .reference(memberRefs.get(i))
                        .fc(memberFcs.get(i)))
                    .collect(Collectors.toList())
            );
    }
}
