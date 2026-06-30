package com.ysh.jcms.app.handler.dataset.createDataSet;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class CreateDataSetDao {
    private String datasetReference;
    private String referenceAfter;
    private List<MemberRef> members = new ArrayList<>();

    @Setter
    @Getter
    @Accessors(fluent = true)
    public static class MemberRef {
        private String reference;
        private int fc;
    }

    public CreateDataSetDao addMember(String reference, int fc) {
        members.add(new MemberRef().reference(reference).fc(fc));
        return this;
    }
}
