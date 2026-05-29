package com.ysh.jcms.datatypes.type;

public interface CmsType {

    byte[] encode();

    CmsType copy();
}
