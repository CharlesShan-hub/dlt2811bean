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

    private int objectClass = CmsObjectClass.LOGICAL_DEVICE;

    private String referenceAfter = null;

    /**
     * Set the reference after cursor. Ignores null/empty values so callers can pass
     * raw CLI args without checking.
     */
    public SvrDirDao referenceAfter(String referenceAfter) {
        if (referenceAfter != null && !referenceAfter.isEmpty()) {
            this.referenceAfter = referenceAfter;
        }
        return this;
    }

    @Override
    public CmsType toRequest() {
        return new CmsGetServerDirectoryRequest().objectClass(objectClass).referenceAfter(referenceAfter);
    }
}
