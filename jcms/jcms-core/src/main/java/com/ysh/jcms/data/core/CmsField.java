package com.ysh.jcms.data.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a public field in a {@link CmsSequence} subclass for automatic
 * injection and sync by the base class constructor.
 *
 * <p>For single {@link CmsType} fields, the inner reference is automatically
 * bound and synced. For {@link #sequenceOf()} fields (SEQUENCE OF), the
 * {@link #elementType()} specifies the CmsType wrapper class for each element.
 *
 * <p>If {@link #optional()} is true, the parent automatically syncs
 * {@code innerCache["has&lt;FieldName&gt;"]} with the Inner* {@code _set}
 * during encode/decode.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CmsField {
    /** True if this field is OPTIONAL in the ASN.1 SEQUENCE. */
    boolean optional() default false;

    /** True if this field is SEQUENCE OF (List of CmsType elements). */
    boolean sequenceOf() default false;

    /** For {@code sequenceOf = true}: the CmsType class of each element. */
    Class<? extends CmsType> elementType() default CmsType.class;

    /**
     * Inner* field name when it differs from the Cms field name.
     * E.g. Cms field {@code refAfter} → Inner field {@code referenceAfter}.
     */
    String inner() default "";
}
