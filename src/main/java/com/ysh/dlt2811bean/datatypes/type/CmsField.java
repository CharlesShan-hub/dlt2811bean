package com.ysh.dlt2811bean.datatypes.type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CmsField {
    boolean optional() default false;
    /** Message type names this field belongs to.
     *  Empty = all types. E.g. {"REQUEST"}, {"RESPONSE_POSITIVE"} */
    String[] only() default {};
}