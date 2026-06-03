package com.ysh.jcms.datatypes.type;

public abstract class AbstractCmsCodedEnum<T extends AbstractCmsCodedEnum<T>>
        extends AbstractCmsScalar<T, Integer> implements CmsCodedEnum<T> {

    protected final int size;

    protected AbstractCmsCodedEnum(String typeName, int value, int size) {
        super(typeName, 0);
        this.size = size;
        set(value);
    }

    @Override
    public void set(Integer value) {
        if (value < 0 || (size > 0 && value >= (1 << size))) {
            throw new IllegalArgumentException("value 0x" + Integer.toHexString(value)
                + " exceeds " + size + "-bit width");
        }
        super.set(value);
    }

    @Override
    public boolean testBit(int pos) {
        checkBitPos(pos);
        return (value & (1 << pos)) != 0;
    }

    @Override
    public void setBit(int pos, boolean val) {
        checkBitPos(pos);
        if (val) value |= (1 << pos);
        else     value &= ~(1 << pos);
        present = true;
    }

    @Override
    public int getBits(int pos, int width) {
        int mask = (1 << width) - 1;
        return (value >>> pos) & mask;
    }

    @Override
    public boolean testBits(int pos, int width, int fieldValue) {
        int mask = (1 << width) - 1;
        return ((value >>> pos) & mask) == (fieldValue & mask);
    }

    @Override
    public void setBits(int pos, int width, int fieldValue) {
        int mask = (1 << width) - 1;
        value &= ~(mask << pos);
        value |= ((fieldValue & mask) << pos);
        present = true;
    }

    /** Convert LSB-0 int value to PER BIT STRING bytes (MSB-first). */
    protected byte[] toPerBytes() {
        int nbytes = (size + 7) / 8;
        int totalBits = nbytes * 8;
        int shift = totalBits - size;
        int shifted = value << shift;
        byte[] bytes = new byte[nbytes];
        for (int i = 0; i < nbytes; i++) {
            bytes[i] = (byte) ((shifted >> (8 * (nbytes - 1 - i))) & 0xFF);
        }
        return bytes;
    }

    /** Convert PER BIT STRING bytes (MSB-first) to LSB-0 int value. */
    protected static int fromPerBytes(byte[] bytes, int size) {
        int nbytes = (size + 7) / 8;
        int totalBits = nbytes * 8;
        int shift = totalBits - size;
        int val = 0;
        for (int i = 0; i < nbytes; i++) {
            val = (val << 8) | (bytes[i] & 0xFF);
        }
        return val >>> shift;
    }

    private void checkBitPos(int pos) {
        if (pos < 0 || pos >= size) {
            throw new IllegalArgumentException("bit position out of range [0, " + (size - 1) + "]: " + pos);
        }
    }
}
