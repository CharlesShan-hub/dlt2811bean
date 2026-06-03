package com.ysh.dlt2811bean.datatypes.type;

public interface CmsCodedEnum<T extends CmsCodedEnum<T>> extends CmsScalar<T, Long> {
    boolean testBit(int pos);
    T setBit(int pos, boolean value);
    long getBits(int pos, int width);
    boolean testBits(int pos, int width, int fieldValue);
    T setBits(int pos, int width, int fieldValue);
}