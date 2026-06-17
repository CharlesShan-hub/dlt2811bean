package com.ysh.jcms.data.string;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.ysh.jcms.core.CmsType;

public class CmsUint8Array2 extends CmsType {

    // Native memory layout of cms_uint8_array_t: 
    // pointer(8) + int32(4) + padding(4) = 16 bytes
    public static final int SIZEOF = 16;

    /** Pointer to the actual data buffer (native heap). */
    public Pointer value;

    /** Current data length in bytes. */
    public int len;

    /** Holds the allocated buffer to prevent GC. */
    protected Memory ownedData;

    /** Default capacity when no size is specified. */
    private static final int DEFAULT_CAPACITY = 256;

    // ==================== Constructors ====================

    /**
     * No-arg constructor: allocates a default-sized buffer.
     */
    public CmsUint8Array2() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructor with a specified buffer capacity.
     *
     * @param capacity  expected max data length, buffer is pre-allocated
     */
    public CmsUint8Array2(int capacity) {
        int size = Math.max(capacity, 1);
        this.ownedData = new Memory(size);
        this.ownedData.setByte(0, (byte) 0);   // null-terminate → empty string
        this.value = ownedData;
        this.len = 0;
        write();
    }
}
