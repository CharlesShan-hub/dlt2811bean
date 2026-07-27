package com.ysh.jcms.util;

import java.nio.ByteBuffer;

/**
 * Byte-level utilities for packed OCTET STRING types (UtcTime, BinaryTime, etc.).
 *
 * <p>Supplements {@link ByteBuffer} with unsigned and 24-bit operations that
 * the JDK does not provide natively.
 */
public final class CmsBytesUtil {

    private CmsBytesUtil() {}

    // ── unsigned int24 ────────────────────────────────────────────────

    /** Read 3 bytes as unsigned int24 (big-endian). */
    public static int getInt24(ByteBuffer buf) {
        return ((buf.get() & 0xFF) << 16) | ((buf.get() & 0xFF) << 8) | (buf.get() & 0xFF);
    }

    /** Write an unsigned int24 as 3 bytes (big-endian). */
    public static void putInt24(ByteBuffer buf, int v) {
        buf.put((byte) (v >> 16)).put((byte) (v >> 8)).put((byte) v);
    }

    // ── unsigned int32 ────────────────────────────────────────────────

    /** Read 4 bytes as unsigned int32 (big-endian), returns long. */
    public static long getInt32u(ByteBuffer buf) {
        return buf.getInt() & 0xFFFFFFFFL;
    }

    /** Write an unsigned int32 (long) as 4 bytes (big-endian). */
    public static void putInt32u(ByteBuffer buf, long v) {
        buf.putInt((int) v);
    }
}
