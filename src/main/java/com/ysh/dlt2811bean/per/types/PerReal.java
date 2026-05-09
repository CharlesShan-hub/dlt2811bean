package com.ysh.dlt2811bean.per.types;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * DL/T 2811 Float32 / Float64 — IEEE 754 codec.
 *
 * <p><b>NOTE: This is NOT a standard ASN.1 PER REAL (X.691 §19) codec.
 * It is purpose-built for the custom Float32 / Float64 data types
 * defined in the DL/T 2811 protocol.</b>
 *
 * <p>The standard ASN.1 REAL type uses base/exponent/mantissa encoding,
 * whereas DL/T 2811's CmsFloat32/CmsFloat64 map directly to IEEE 754
 * binary representation. Hence this class uses a simplified encoding:
 * 1-bit zero flag + aligned IEEE 754 bytes.
 *
 * <p>DL/T 2811 usage:
 * <ul>
 *   <li>FLOAT32 — 32-bit floating-point analog value (7.1.4)</li>
 *   <li>FLOAT64 — 64-bit floating-point analog value (7.1.4, high-precision)</li>
 * </ul>
 *
 * <p>Encoding:
 * <ul>
 *   <li>Zero value: 1 bit (0)</li>
 *   <li>Non-zero value: 1 bit (1) + IEEE 754 bytes after alignment</li>
 * </ul>
 *
 * <h3>Examples</h3>
 * <pre>{@code
 *   // --- FLOAT64 (double) ---
 *   PerOutputStream pos = new PerOutputStream();
 *   PerReal.encodeFloat64(pos, 220.5);
 *   byte[] data = pos.toByteArray();
 *
 *   PerInputStream pis = new PerInputStream(data);
 *   double voltage = PerReal.decodeFloat64(pis);  // 220.5
 *
 *   // --- FLOAT32 (float) ---
 *   PerOutputStream pos2 = new PerOutputStream();
 *   PerReal.encodeFloat32(pos2, 3.14f);
 *   byte[] data2 = pos2.toByteArray();
 *
 *   PerInputStream pis2 = new PerInputStream(data2);
 *   float val = PerReal.decodeFloat32(pis2);  // 3.14f
 * }</pre>
 */
public final class PerReal {

    private PerReal() { /* utility class */ }

    // ==================== Float64 (double) ====================

    /**
     * Encodes a 64-bit double value (IEEE 754 double-precision, 8 bytes).
     *
     * @param pos   output stream
     * @param value double value
     */
    public static void encodeFloat64(PerOutputStream pos, double value) {
        if (value == 0.0) {
            pos.writeBit(false);
            return;
        }

        pos.writeBit(true);
        writeIeee754(pos, value);
    }

    /**
     * Decodes a 64-bit double value.
     *
     * @param pis input stream
     * @return decoded double value
     * @throws PerDecodeException if insufficient data
     */
    public static double decodeFloat64(PerInputStream pis) throws PerDecodeException {
        boolean isNonZero = pis.readBit();
        if (!isNonZero) {
            return 0.0;
        }
        return readIeee754(pis);
    }

    // ==================== Float32 (float) ====================

    /**
     * Encodes a 32-bit float value (IEEE 754 single-precision, 4 bytes).
     *
     * @param pos   output stream
     * @param value float value
     */
    public static void encodeFloat32(PerOutputStream pos, float value) {
        if (value == 0.0f) {
            pos.writeBit(false);
            return;
        }

        pos.writeBit(true);
        pos.align();
        int bits = Float.floatToIntBits(value);
        for (int i = 3; i >= 0; i--) {
            pos.writeByteAligned((byte) ((bits >> (i * 8)) & 0xFF));
        }
    }

    /**
     * Decodes a 32-bit float value.
     *
     * @param pis input stream
     * @return decoded float value
     * @throws PerDecodeException if insufficient data
     */
    public static float decodeFloat32(PerInputStream pis) throws PerDecodeException {
        boolean isNonZero = pis.readBit();
        if (!isNonZero) {
            return 0.0f;
        }

        pis.align();
        int bits = 0;
        for (int i = 0; i < 4; i++) {
            bits = (bits << 8) | (pis.readByteAligned() & 0xFF);
        }
        return Float.intBitsToFloat(bits);
    }

    // ==================== Internal ====================

    private static void writeIeee754(PerOutputStream pos, double value) {
        long bits = Double.doubleToLongBits(value);
        pos.align();
        for (int i = 7; i >= 0; i--) {
            byte b = (byte) ((bits >> (i * 8)) & 0xFF);
            pos.writeByteAligned(b);
        }
    }

    private static double readIeee754(PerInputStream pis) throws PerDecodeException {
        pis.align();
        long bits = 0;
        for (int i = 0; i < 8; i++) {
            bits = (bits << 8) | (pis.readByteAligned() & 0xFFL);
        }
        return Double.longBitsToDouble(bits);
    }
}