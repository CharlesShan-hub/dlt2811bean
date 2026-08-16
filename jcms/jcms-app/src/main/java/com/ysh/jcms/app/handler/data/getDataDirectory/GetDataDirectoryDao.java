package com.ysh.jcms.app.handler.data.getDataDirectory;

import com.ysh.jcms.app.handler.base.BaseDao;
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
        CmsGetDataDirectoryRequest req = new CmsGetDataDirectoryRequest().dataReference(dataReference);
        setIfNotEmpty(req::referenceAfter, referenceAfter);
        return req;
    }
}
