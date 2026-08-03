package com.ysh.jcms.pdu.file;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerGetFileDirectoryRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsString;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;

/**
 * GetFileDirectory-RequestPDU ::= SEQUENCE { pathName [0] IMPLICIT
 * VisibleString (SIZE (0..255)), startTime [1] IMPLICIT TimeStamp OPTIONAL,
 * stopTime [2] IMPLICIT TimeStamp OPTIONAL, fileAfter [3] IMPLICIT
 * VisibleString (SIZE (0..255)) OPTIONAL } — 8.12.5
 */
public class CmsGetFileDirectoryRequest extends CmsSequence {

    @CmsField
    public CmsString pathName;
    @CmsField(optional = true)
    public CmsUtcTime startTime;
    @CmsField(optional = true)
    public CmsUtcTime stopTime;
    @CmsField(optional = true)
    public CmsString fileAfter;

    public CmsGetFileDirectoryRequest() {
        super(new InnerGetFileDirectoryRequestPDU());
    }

    public CmsGetFileDirectoryRequest pathName(String v) {
        this.pathName.value(v);
        return this;
    }
    public CmsGetFileDirectoryRequest pathName(byte[] v) {
        return pathName(new String(v, StandardCharsets.UTF_8));
    }
    public CmsGetFileDirectoryRequest startTime(CmsUtcTime v) {
        if (v != null) {
            this.startTime.value(v);
            setPresent("startTime", true);
        } else {
            setPresent("startTime", false);
        }
        return this;
    }
    public CmsGetFileDirectoryRequest stopTime(CmsUtcTime v) {
        if (v != null) {
            this.stopTime.value(v);
            setPresent("stopTime", true);
        } else {
            setPresent("stopTime", false);
        }
        return this;
    }
    public CmsGetFileDirectoryRequest fileAfter(String v) {
        if (v != null) {
            this.fileAfter.value(v);
            setPresent("fileAfter", true);
        } else {
            setPresent("fileAfter", false);
        }
        return this;
    }
    public CmsGetFileDirectoryRequest fileAfter(byte[] v) {
        return fileAfter(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
}
