package com.ysh.jcms.app.handler.data.getDataDefinition;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.data.CmsDataRefEntry;
import com.ysh.jcms.pdu.data.CmsGetDataDefinitionRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetDataDefinitionDao extends BaseDao {

    public GetDataDefinitionDao() {
        paginationContext(new PaginationContext());
    }

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

    @Override
    public CmsType toRequest() {
        CmsGetDataDefinitionRequest req = new CmsGetDataDefinitionRequest();
        for (DataRef ref : dataRefs) {
            CmsDataRefEntry entry = new CmsDataRefEntry().reference(ref.reference());
            if (ref.fc() != null) {
                entry.fc(ref.fc());
            }
            req.data.add(entry);
        }
        return req;
    }
}
