package com.ysh.jcms.datatypes.data;

import com.ysh.jcms.datatypes.type.AbstractCmsType;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Abstract base for CmsData and CmsDataDefinition.
 *
 * <p>Shared choice constants, choice field, and abstract encode/copy contract.
 * Part of the jcms type hierarchy via {@link AbstractCmsType}.
 */
@Getter
@Accessors(fluent = true)
public abstract class AbstractCmsDataUnit<T extends AbstractCmsDataUnit<T>>
        extends AbstractCmsType<T> {

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

    public int choice;
    public int serviceError;

    protected AbstractCmsDataUnit(String typeName) {
        super(typeName);
    }

    public abstract boolean isTagOnly();
}
