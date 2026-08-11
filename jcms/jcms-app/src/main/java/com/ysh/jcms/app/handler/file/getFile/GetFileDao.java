package com.ysh.jcms.app.handler.file.getFile;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.pdu.file.CmsGetFileRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetFileDao extends BaseDao {
    private String fileName;
    private String outputFile; // local path to save, null to print info only

    @Override
    public CmsType toRequest() {
        return new CmsGetFileRequest().filename(fileName).startPosition(1);
    }
}
