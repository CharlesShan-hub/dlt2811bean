package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.svc.directory.CmsObjectClass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetServerDirectoryDao {

    private int objectClass = CmsObjectClass.LOGICAL_DEVICE;
    private String referenceAfter;
}
