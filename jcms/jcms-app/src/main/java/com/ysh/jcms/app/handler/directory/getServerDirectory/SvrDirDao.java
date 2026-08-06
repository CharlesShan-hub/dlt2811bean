package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.data.enumerate.CmsObjectClass;
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
}
