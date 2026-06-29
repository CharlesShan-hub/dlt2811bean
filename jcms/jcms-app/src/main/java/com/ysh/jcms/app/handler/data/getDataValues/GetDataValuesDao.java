package com.ysh.jcms.app.handler.data.getDataValues;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetDataValuesDao {

    /** Data reference entries (reference + optional fc) */
    private List<DataRef> dataRefs = new ArrayList<>();

    @Setter
    @Getter
    @Accessors(fluent = true)
    public static class DataRef {
        /** Object reference (e.g. "LD0/LLN0.Mod") */
        private String reference;
        /** Optional FunctionalConstraint filter (0 or null = no filter) */
        private Integer fc;
    }

    public GetDataValuesDao addRef(String reference) {
        dataRefs.add(new DataRef().reference(reference));
        return this;
    }

    public GetDataValuesDao addRef(String reference, int fc) {
        dataRefs.add(new DataRef().reference(reference).fc(fc));
        return this;
    }
}
