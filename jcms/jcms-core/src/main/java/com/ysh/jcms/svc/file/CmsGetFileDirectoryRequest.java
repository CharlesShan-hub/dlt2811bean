package com.ysh.jcms.svc.file;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.time.CmsUtcTime;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetFileDirectory-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     pathName        [0] IMPLICIT VisibleString255,
 *     startTime       [1] IMPLICIT TimeStamp OPTIONAL,
 *     stopTime        [2] IMPLICIT TimeStamp OPTIONAL,
 *     fileAfter       [3] IMPLICIT VisibleString255 OPTIONAL
 * }  —  8.12.4
 */
public class CmsGetFileDirectoryRequest extends CmsType {

    public CmsReqId       reqId;
    public CmsUint8Array  pathName;
    public CmsBoolean     startTimePresent;
    public CmsUtcTime   startTime;      /* OPTIONAL */
    public CmsBoolean     stopTimePresent;
    public CmsUtcTime   stopTime;       /* OPTIONAL */
    public CmsBoolean     fileAfterPresent;
    public CmsUint8Array  fileAfter;      /* OPTIONAL */

    public CmsGetFileDirectoryRequest() { super(Codec.GET_FILE_DIRECTORY_REQUEST);
        this.reqId            = new CmsReqId();
        this.pathName         = new CmsUint8Array();
        this.startTimePresent = new CmsBoolean();
        this.startTime        = new CmsUtcTime();
        this.stopTimePresent  = new CmsBoolean();
        this.stopTime         = new CmsUtcTime();
        this.fileAfterPresent = new CmsBoolean();
        this.fileAfter        = new CmsUint8Array();
    }
    
    public CmsGetFileDirectoryRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetFileDirectoryRequest pathName(byte[] v) { this.pathName.value(v); return this; }
    public CmsGetFileDirectoryRequest pathName(String v) { this.pathName.value(v); return this; }
    public CmsGetFileDirectoryRequest startTimePresent(boolean v) { this.startTimePresent.value(v); return this; }
    public CmsGetFileDirectoryRequest startTime(CmsUtcTime v) { this.startTime = v; return this; }
    public CmsGetFileDirectoryRequest stopTimePresent(boolean v) { this.stopTimePresent.value(v); return this; }
    public CmsGetFileDirectoryRequest stopTime(CmsUtcTime v) { this.stopTime = v; return this; }
    public CmsGetFileDirectoryRequest fileAfterPresent(boolean v) { this.fileAfterPresent.value(v); return this; }
    public CmsGetFileDirectoryRequest fileAfter(byte[] v) { this.fileAfterPresent.value(v != null && v.length > 0); if (v != null) this.fileAfter.value(v); return this; }
    public CmsGetFileDirectoryRequest fileAfter(String v) { this.fileAfterPresent.value(v != null); if (v != null) this.fileAfter.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, pathName,
            startTimePresent, startTime,
            stopTimePresent, stopTime,
            fileAfterPresent, fileAfter);
    }
}