package com.ysh.jcms.app.handler.data.getDataValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.data.CmsDataRefEntry;
import com.ysh.jcms.pdu.data.CmsGetDataValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetDataValuesDao extends BaseDao {

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

    @Override
    public CmsType toRequest() {
        CmsGetDataValuesRequest req = new CmsGetDataValuesRequest();
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
