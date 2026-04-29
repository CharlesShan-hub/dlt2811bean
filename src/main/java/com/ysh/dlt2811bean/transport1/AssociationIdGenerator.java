package com.ysh.dlt2811bean.transport1;

import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Utility class for generating association IDs.
 *
 * <p>Association ID is a 64-byte OCTET STRING used to identify an active
 * association context between client and server (GB/T 45906.3-2025 Table 19).
 * The format is not specified by the standard; this generator uses a combination
 * of timestamp, UUID, and secure random to ensure uniqueness.</p>
 */
public final class AssociationIdGenerator {

    private static final int SIZE = CmsAssociate.ASSOC_ID_SIZE; // 64
    private static final SecureRandom RANDOM = new SecureRandom();

    private AssociationIdGenerator() {
        // utility class
    }

    /**
     * Generate a new 64-byte association ID.
     *
     * <p>Format (all big-endian):
     * <pre>
     * Bytes 0..7   : Unix timestamp in milliseconds
     * Bytes 8..23  : 16-byte UUID (128 bits)
     * Bytes 24..55 : 32 bytes from SecureRandom
     * Bytes 56..63 : 8 bytes from SecureRandom (remainder)
     * </pre>
     *
     * @return a new 64-byte array
     */
    public static byte[] generate() {
        byte[] id = new byte[SIZE];

        // Bytes 0-7: timestamp (ms)
        long timestamp = System.currentTimeMillis();
        for (int i = 0; i < 8; i++) {
            id[7 - i] = (byte) (timestamp >>> (i * 8));
        }

        // Bytes 8-23: UUID
        UUID uuid = UUID.randomUUID();
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        for (int i = 0; i < 8; i++) {
            id[15 - i] = (byte) (msb >>> (i * 8));
            id[23 - i] = (byte) (lsb >>> (i * 8));
        }

        // Bytes 24-63: random fill (32 bytes)
        byte[] randomBytes = new byte[SIZE - 24];
        RANDOM.nextBytes(randomBytes);
        System.arraycopy(randomBytes, 0, id, 24, randomBytes.length);

        return id;
    }
}
