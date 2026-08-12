package com.ysh.jcms.core.data.sequence.block;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注控制块字段的生命周期层级，配合 {@link CbFieldScope} 使用。
 * <p>
 * 与 {@code @CmsField}（APER 编码字段标记）并存：@CmsField 描述"这个字段参与编码"，
 *
 * @CbField 描述"这个字段活在哪个生命周期层级"。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CbField {
    CbFieldScope scope() default CbFieldScope.ENGINEERING;
}
