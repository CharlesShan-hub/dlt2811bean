package com.ysh.jcms.datatypes.type;

public interface CmsType<T extends CmsType<T>> {

    byte[] encode();

    T copy();
}
