package com.ysh.jcms.app.handler.sg.getEditSgValue;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetEditSgValueDao extends BaseDao {
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
}
