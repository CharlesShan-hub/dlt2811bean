package com.ysh.jcms.app.handler.sg.getEditSgValue;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.sg.CmsSgRefFcEntry;
import com.ysh.jcms.pdu.sg.CmsGetEditSgValueRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetEditSgValueDao extends BaseDao {
    public GetEditSgValueDao() {
        paginationContext(new PaginationContext());
    }

    private List<RefFcPair> refs = new ArrayList<>();

    @Setter
    @Getter
    @Accessors(fluent = true)
    public static class RefFcPair {
        private String reference;
        private Integer fc;
    }

    public GetEditSgValueDao addRef(String reference, int fc) {
        refs.add(new RefFcPair().reference(reference).fc(fc));
        return this;
    }

    @Override
    public CmsType toRequest() {
        CmsGetEditSgValueRequest req = new CmsGetEditSgValueRequest();
        for (RefFcPair pair : refs) {
            req.data.add(new CmsSgRefFcEntry().reference(pair.reference()).fc(pair.fc()));
        }
        return req;
    }
}
