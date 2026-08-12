package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.directory.CmsGetLogicalDeviceDirectoryRequest;
import lombok.Getter;
import com.ysh.jcms.app.handler.BaseDao;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class LdDirDao extends BaseDao {
    private String ldName;
    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        CmsGetLogicalDeviceDirectoryRequest req = new CmsGetLogicalDeviceDirectoryRequest().referenceAfter(referenceAfter);
        if (ldName != null) {
            req.ldName(ldName);
        }
        return req;
    }
}
