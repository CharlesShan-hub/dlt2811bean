package com.ysh.jcms.utils.transport.session;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Utility for generating 64-byte association IDs.
 *
 * <p>Format:
 * <pre>
 * Bytes 0..7   : Unix timestamp (ms)
 * Bytes 8..39  : UUID (most significant + least significant bits)
 * Bytes 40..63 : Secure random
 * </pre>
 */
public final class AssociationIdGenerator {

    private static final int SIZE = 64;
    private static final SecureRandom RANDOM = new SecureRandom();

    private AssociationIdGenerator() {}

    public static byte[] generate() {
        byte[] id = new byte[SIZE];
        long ts = System.currentTimeMillis();
        for (int i = 0; i < 8; i++) id[i] = (byte) (ts >> (56 - i * 8));

        UUID uuid = UUID.randomUUID();
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        for (int i = 0; i < 8; i++) {
            id[8 + i] = (byte) (msb >> (56 - i * 8));
            id[16 + i] = (byte) (lsb >> (56 - i * 8));
        }

        byte[] random = new byte[24];
        RANDOM.nextBytes(random);
        System.arraycopy(random, 0, id, 40, 24);
        return id;
    }
}
