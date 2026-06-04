package com.ysh.jcms.datatypes.data;

import com.ysh.jcms.datatypes.type.CmsType;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Abstract base for CmsData and CmsDataDefinition.
 *
 * <p>Shared choice constants, choice field, and abstract encode/copy contract.
 */
@Getter
@Accessors(fluent = true)
public abstract class AbstractCmsDataUnit<T extends AbstractCmsDataUnit<T>>
        implements CmsType<T> {

    public static final int ERROR          = 0;
    public static final int ARRAY          = 1;
    public static final int STRUCTURE      = 2;
    public static final int BOOLEAN        = 3;
    public static final int INT8           = 4;
    public static final int INT16          = 5;
    public static final int INT32          = 6;
    public static final int INT64          = 7;
    public static final int INT8U          = 8;
    public static final int INT16U         = 9;
    public static final int INT32U         = 10;
    public static final int INT64U         = 11;
    public static final int FLOAT32        = 12;
    public static final int FLOAT64        = 13;
    public static final int BIT_STRING     = 14;
    public static final int OCTET_STRING   = 15;
    public static final int VISIBLE_STRING = 16;
    public static final int UTF8_STRING    = 17;
    public static final int UTC_TIME       = 18;
    public static final int BINARY_TIME    = 19;
    public static final int QUALITY        = 20;
    public static final int DBPOS          = 21;
    public static final int TCMD           = 22;
    public static final int CHECK          = 23;

    protected final String typeName;
    protected boolean optional = false;
    protected boolean present = true;
    public int choice;
    public int serviceError;

    protected AbstractCmsDataUnit(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean isOptional() { return optional; }

    @Override
    public void setOptional(boolean optional) { this.optional = optional; }

    @Override
    public boolean isPresent() { return present; }

    @Override
    public void setPresent(boolean present) { this.present = present; }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    public abstract boolean isTagOnly();
}
