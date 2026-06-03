package com.ysh.jcms.datatypes.type;

public interface CmsCodedEnum<T extends CmsCodedEnum<T>> extends CmsScalar<T, Integer> {
    boolean testBit(int pos);
    void setBit(int pos, boolean value);
    int getBits(int pos, int width);
    boolean testBits(int pos, int width, int fieldValue);
    void setBits(int pos, int width, int fieldValue);
}
