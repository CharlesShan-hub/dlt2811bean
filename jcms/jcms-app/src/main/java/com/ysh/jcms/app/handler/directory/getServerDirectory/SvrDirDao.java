package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.enumerate.CmsObjectClass;
import com.ysh.jcms.core.pdu.directory.CmsGetServerDirectoryRequest;
import com.ysh.jcms.app.handler.base.BaseDao;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(fluent = true)
public class SvrDirDao extends BaseDao {

    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        return new CmsGetServerDirectoryRequest()
            .objectClass(CmsObjectClass.LOGICAL_DEVICE)
            .referenceAfter(referenceAfter);
    }
}
