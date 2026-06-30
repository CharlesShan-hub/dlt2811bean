package com.ysh.jcms.app.handler.data.getDataDefinition;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetDataDefinitionDao {

    private List<DataRef> dataRefs = new ArrayList<>();

    @Setter
    @Getter
    @Accessors(fluent = true)
    public static class DataRef {
        private String reference;
        private Integer fc;
    }

    public GetDataDefinitionDao addRef(String reference) {
        dataRefs.add(new DataRef().reference(reference));
        return this;
    }

    public GetDataDefinitionDao addRef(String reference, int fc) {
        dataRefs.add(new DataRef().reference(reference).fc(fc));
        return this;
    }
}
