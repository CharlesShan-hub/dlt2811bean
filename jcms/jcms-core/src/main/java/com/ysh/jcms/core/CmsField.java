package com.ysh.jcms.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field in a {@link CmsSequence} subclass for automatic
 * {@link CmsSequence#syncToInner() sync} / {@link CmsSequence#syncFromInner()
 * syncFromInner} with the backing Inner* PDU.
 *
 * <p>Examples:
 * <pre>{@code
 * @CmsField
 * public int objectClass;
 *
 * @CmsField(inner = "referenceAfter", optional = true)
 * public CmsObjectReference refAfter;
 *
 * @CmsField(sequenceOf = true, elementType = CmsObjectReference.class)
 * public List<CmsObjectReference> reference;
 * }</pre>
 *
 * <p>For {@code optional = true} fields, use {@link CmsSequence#setPresent}
 * to mark presence:
 * <pre>{@code
 * setPresent("refAfter", v != null);
 * }</pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CmsField {

    /** Inner* PDU field name. Defaults to the Java field name. */
    String inner() default "";

    /** Whether this field is OPTIONAL in the ASN.1 definition. */
    boolean optional() default false;

    /**
     * ASN.1 DEFAULT value, as a string.
     * For example, {@code "true"} for {@code BOOLEAN DEFAULT TRUE}.
     */
    String defaultAsn1() default "";

    /** Whether this field is a SEQUENCE OF (i.e. a {@link java.util.List}). */
    boolean sequenceOf() default false;

    /** The element type of the list when {@link #sequenceOf()} is true. */
    Class<?> elementType() default void.class;
}
