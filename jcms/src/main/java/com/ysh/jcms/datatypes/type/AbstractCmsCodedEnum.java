package com.ysh.jcms.datatypes.type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // ==================== toString ====================

    @Override
    public String toString() {
        Map<Integer, String> names = nameMap();
        List<String> setFlags = new ArrayList<>();
        for (int pos = 0; pos < size; pos++) {
            if ((value & (1 << pos)) != 0) {
                String name = names.get(pos);
                setFlags.add(name != null ? name : "bit" + pos);
            }
        }
        String binary = toBinary(value, size);
        if (setFlags.isEmpty()) {
            return "(" + getClass().getSimpleName() + ") " + binary;
        }
        return "(" + getClass().getSimpleName() + ") " + binary
            + " [" + String.join(", ", setFlags) + "]";
    }

    /** Format value as binary string, grouped by 4 nibbles. */
    private static String toBinary(int value, int size) {
        if (size <= 4) {
            return "0b" + Integer.toBinaryString(value);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = size - 1; i >= 0; i--) {
            sb.append((value >> i) & 1);
            if (i % 4 == 0 && i != 0) sb.append(' ');
        }
        return "0b" + sb;
    }

    // ==================== Bit-name mapping ====================

    @SuppressWarnings("unchecked")
    private Map<Integer, String> nameMap() {
        Map<Integer, String> cache = (Map<Integer, String>) cacheGet();
        if (cache != null) return cache;
        cache = buildNameMap();
        cachePut(cache);
        return cache;
    }

    private Map<Integer, String> buildNameMap() {
        Map<Integer, String> map = new HashMap<>();
        for (Field field : getClass().getFields()) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isFinal(mod) && field.getType() == int.class) {
                try {
                    int val = field.getInt(null);
                    map.put(val, field.getName());
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return map;
    }

    // ==================== Per-class cache ====================

    private static final Map<Class<?>, Object> CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    private Object cacheGet() {
        return CACHE.get(getClass());
    }

    private void cachePut(Object cache) {
        CACHE.put(getClass(), cache);
    }

    // ==================== Encode / Decode helpers ====================

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
