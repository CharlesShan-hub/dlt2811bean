package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.InnerGetAllCBValuesErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetAllCBValues-ErrorPDU ::= ServiceError — 8.3.6
 *
 * <p>Type alias, not a SEQUENCE.
 */
public class CmsGetAllCbValuesError extends CmsServiceError {

    public CmsGetAllCbValuesError() {
        super(new InnerGetAllCBValuesErrorPDU());
    }

    @Override
    public CmsGetAllCbValuesError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
