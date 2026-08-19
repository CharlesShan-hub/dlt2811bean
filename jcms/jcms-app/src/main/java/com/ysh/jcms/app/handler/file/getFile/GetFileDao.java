package com.ysh.jcms.app.handler.file.getFile;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.file.CmsGetFileRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetFileDao extends BaseDao {

    /** File name on server to download */
    private String fileName;

    /** Local path to save (null to print info only) */
    private String outputFile;

    /** Download start position, advanced per chunk */
    private long position = 1;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(fileName, "fileName must not be null");
        return new CmsGetFileRequest().filename(fileName).startPosition(position);
    }
}
