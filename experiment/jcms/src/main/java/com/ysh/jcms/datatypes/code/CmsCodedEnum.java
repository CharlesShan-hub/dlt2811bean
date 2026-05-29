package com.ysh.jcms.datatypes.code;

import com.ysh.jcms.datatypes.type.CmsScalar;

public interface CmsCodedEnum extends CmsScalar<Long> {
    boolean testBit(int pos);
    void setBit(int pos, boolean value);
    long getBits(int pos, int width);
    boolean testBits(int pos, int width, int fieldValue);
    void setBits(int pos, int width, int fieldValue);
}
