package com.ysh.jcms.app.handler.data.getDataDirectory;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.data.CmsGetDataDirectoryRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetDataDirectoryDao extends BaseDao {

    private String dataReference;
    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        return new CmsGetDataDirectoryRequest().dataReference(dataReference).referenceAfter(referenceAfter);
    }
}
