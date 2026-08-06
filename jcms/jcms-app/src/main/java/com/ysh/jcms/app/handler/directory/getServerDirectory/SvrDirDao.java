package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsObjectClass;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryRequest;
import lombok.Getter;
import com.ysh.jcms.app.handler.BaseDao;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SvrDirDao extends BaseDao {

    private int objectClass = CmsObjectClass.LOGICAL_DEVICE;
    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        return new CmsGetServerDirectoryRequest().objectClass(objectClass).referenceAfter(referenceAfter);
    }
}
