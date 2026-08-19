package com.ysh.jcms.app.handler.file.setFile;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.file.CmsSetFileRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetFileDao extends BaseDao {

    /** Remote path on server (required) */
    private String remoteFile;

    /** Local file to upload */
    private String localFile;

    /** Upload start position, advanced per chunk */
    private long position = 1;

    /** Chunk payload */
    private byte[] fileData;

    /** Marks the final chunk */
    private boolean endOfFile;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(remoteFile, "remoteFile must not be null");
        return new CmsSetFileRequest()
            .filename(remoteFile)
            .startPosition(position)
            .fileData(fileData)
            .endOfFile(endOfFile);
    }
}
