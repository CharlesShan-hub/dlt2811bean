package com.ysh.jcms.app.handler.file.getFileDirectory;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.file.CmsGetFileDirectoryRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetFileDirectoryDao extends BaseDao {
    private String pathName;
    private String startTime;
    private String stopTime;
    private String fileAfter;

    @Override
    public CmsType toRequest() {
        CmsGetFileDirectoryRequest req = new CmsGetFileDirectoryRequest();
        if (pathName != null && !pathName.isEmpty())
            req.pathName(pathName);
        if (fileAfter != null && !fileAfter.isEmpty())
            req.fileAfter(fileAfter);
        // startTime/stopTime would need parsing — skipped for CLI simplicity
        return req;
    }
}
