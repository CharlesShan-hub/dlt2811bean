package com.ysh.jcms.app.handler.sg.getEditSgValue;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.scalar.CmsFC;
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

    private List<String> refs = new ArrayList<>();
    private String fc = "SG";

    @Override
    public CmsType toRequest() {
        int fcCode = CmsFC.fromString(fc);
        CmsGetEditSgValueRequest req = new CmsGetEditSgValueRequest();
        for (String ref : refs) {
            req.data.add(new CmsSgRefFcEntry().reference(ref).fc(fcCode));
        }
        return req;
    }
}
