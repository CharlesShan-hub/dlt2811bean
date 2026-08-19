package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.directory.CmsGetLogicalDeviceDirectoryRequest;
import com.ysh.jcms.app.handler.base.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class LdDirDao extends BaseDao {

    /** Logical device name (e.g. "LD0"), optional — omit to query all LDs */
    private String ldName;

    /** Optional pagination: return items after this reference */
    private String referenceAfter;

    @Override
    public CmsType toRequest() {
        return new CmsGetLogicalDeviceDirectoryRequest()
            .ldName(ldName)
            .referenceAfter(referenceAfter);
    }
}
