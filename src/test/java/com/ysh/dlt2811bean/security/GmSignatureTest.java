package com.ysh.dlt2811bean.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GmSignature.
 */
@DisplayName("GmSignature Tests")
class GmSignatureTest {

    @Test
    @DisplayName("Should generate valid SM2 key pair")
    void testGenerateKeyPair() {
        KeyPair keyPair = GmSignature.generateKeyPair();

        assertNotNull(keyPair);
        assertNotNull(keyPair.getPrivate());
        assertNotNull(keyPair.getPublic());
        assertEquals("SM2", keyPair.getPrivate().getAlgorithm());
        assertEquals("SM2", keyPair.getPublic().getAlgorithm());
    }

    @Test
    @DisplayName("Should sign and verify message")
    void testSignAndVerify() {
        KeyPair keyPair = GmSignature.generateKeyPair();
        byte[] message = "Test message for SM2 signature".getBytes();

        // Sign
        byte[] signature = GmSignature.sign(keyPair.getPrivate(), message);
        assertNotNull(signature);
        assertEquals(64, signature.length); // SM2 signature is 64 bytes

        // Verify
        boolean isValid = GmSignature.verify(keyPair.getPublic(), message, signature);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should fail verification with wrong message")
    void testVerifyWithWrongMessage() {
        KeyPair keyPair = GmSignature.generateKeyPair();
        byte[] message = "Original message".getBytes();
        byte[] wrongMessage = "Tampered message".getBytes();

        byte[] signature = GmSignature.sign(keyPair.getPrivate(), message);
        boolean isValid = GmSignature.verify(keyPair.getPublic(), wrongMessage, signature);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should fail verification with wrong public key")
    void testVerifyWithWrongPublicKey() {
        KeyPair keyPair1 = GmSignature.generateKeyPair();
        KeyPair keyPair2 = GmSignature.generateKeyPair();
        byte[] message = "Test message".getBytes();

        byte[] signature = GmSignature.sign(keyPair1.getPrivate(), message);
        boolean isValid = GmSignature.verify(keyPair2.getPublic(), message, signature);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should compute SM3 hash")
    void testSm3Hash() {
        byte[] data = "Hello, SM3!".getBytes();
        byte[] hash = GmSignature.sm3(data);

        assertNotNull(hash);
        assertEquals(32, hash.length); // SM3 produces 32-byte hash

        // Same input should produce same hash
        byte[] hash2 = GmSignature.sm3(data);
        assertArrayEquals(hash, hash2);
    }

    @Test
    @DisplayName("Should compute SM3 hash as hex string")
    void testSm3Hex() {
        String hex = GmSignature.sm3Hex("test");
        assertNotNull(hex);
        assertEquals(64, hex.length()); // 32 bytes = 64 hex chars

        // Lowercase hex
        assertTrue(hex.matches("[0-9a-f]+"));
    }

    @Test
    @DisplayName("Should create public key from hex")
    void testDecodePublicKey() throws Exception {
        KeyPair keyPair = GmSignature.generateKeyPair();

        // Encode public key to hex (BC format)
        byte[] encodedPubKey = keyPair.getPublic().getEncoded();
        String hex = org.bouncycastle.util.encoders.Hex.toHexString(encodedPubKey);

        // Decode back
        java.security.PublicKey decoded = GmSignature.decodePublicKey(hex);
        assertNotNull(decoded);
        assertEquals(keyPair.getPublic(), decoded);
    }

    @Test
    @DisplayName("Should create private key from hex")
    void testDecodePrivateKey() throws Exception {
        KeyPair keyPair = GmSignature.generateKeyPair();

        // Encode private key to hex
        byte[] encoded = keyPair.getPrivate().getEncoded();
        String hex = org.bouncycastle.util.encoders.Hex.toHexString(encoded);

        // Decode back - this tests the byte array version
        java.security.PrivateKey decoded = GmSignature.decodePrivateKey(hex);
        assertNotNull(decoded);
    }
}
