package com.ysh.jcms.util;

import com.ysh.jcms.ffi.CmsType;

/**
 * 格式化 CmsType 为带缩进的多行文本。
 * 子类可通过构造器设置自己的格式化器覆盖默认行为。
 */
@FunctionalInterface
public interface CmsToString {
    String toString(CmsType obj, int indent);
}
