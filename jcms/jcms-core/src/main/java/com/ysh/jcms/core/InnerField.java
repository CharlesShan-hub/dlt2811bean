package com.ysh.jcms.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a public CmsType field in a {@link CmsSequence} subclass for automatic
 * injection by the base class constructor.
 *
 * <p>The annotated field's {@code inner} reference will point to the
 * corresponding Inner* field, and its {@code innerCache} will be shared
 * under the parent's {@code innerCache} keyed by the field name.
 *
 * <p>If {@link #optional()} is true, the parent automatically syncs
 * {@code innerCache["has&lt;FieldName&gt;"]} with the Inner* {@code _set}
 * during encode/decode.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InnerField {
    /** True if this field is OPTIONAL in the ASN.1 SEQUENCE. */
    boolean optional() default false;
}
