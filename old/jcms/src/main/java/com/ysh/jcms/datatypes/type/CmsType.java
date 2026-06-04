package com.ysh.jcms.datatypes.type;

public interface CmsType<T extends CmsType<T>> {

    byte[] encode();

    T copy();

    /** 是否标记为 OPTIONAL（可选字段）。 */
    boolean isOptional();

    /** 设置 OPTIONAL 标记。 */
    void setOptional(boolean optional);

    /** 该字段在当前上下文中是否存在（编解码时使用）。 */
    boolean isPresent();

    /** 设置字段是否存在。 */
    void setPresent(boolean present);
}
