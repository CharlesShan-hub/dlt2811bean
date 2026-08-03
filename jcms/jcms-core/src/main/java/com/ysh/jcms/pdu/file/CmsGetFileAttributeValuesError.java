package com.ysh.jcms.pdu.file;

import com.ysh.jcms.data.InnerGetFileAttributeValuesErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetFileAttributeValues-ErrorPDU ::= ServiceError — 8.12.4
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetFileAttributeValuesError extends CmsServiceError {

    public CmsGetFileAttributeValuesError() {
        super(new InnerGetFileAttributeValuesErrorPDU());
    }

    public CmsGetFileAttributeValuesError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetFileAttributeValuesError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
