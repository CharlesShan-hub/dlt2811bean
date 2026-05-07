package com.ysh.dlt2811bean.security;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import java.util.Date;

/**
 * 国密签名工具类。
 *
 * <p>提供 SM2 数字签名和验签功能，遵循 GB/T 32918.4-2016 标准。
 * 支持：
 * <ul>
 *   <li>SM2签名 - SM3withSM2 算法</li>
 *   <li>SM3哈希 - 消息摘要</li>
 * </ul>
 *
 * <p>签名数据格式（DL/T 2811-2024）：
 * <pre>
 * ┌─────────────────────────────────────────────────────────────┐
 * │ signatureValue  OCTET STRING (SIZE(64))                     │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * <p>使用示例：
 * <pre>
 * // 签名
 * byte[] signature = GmSignature.sign(privateKey, message);
 *
 * // 验签
 * boolean valid = GmSignature.verify(publicKey, message, signature);
 * </pre>
 */
@Slf4j
public class GmSignature {

    private static final String SIGNATURE_ALGORITHM = "SM3withSM2";
    private static final String KEY_ALGORITHM = "SM2";
    private static final String PROVIDER = "BC";

    static {
        // Register BouncyCastle provider if not already registered
        if (Security.getProvider(PROVIDER) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * 使用 SM2 私钥签名。
     *
     * @param privateKey SM2 私钥
     * @param message    待签名消息
     * @return 64字节签名值 (r || s)
     * @throws SecurityException 如果签名失败
     */
    public static byte[] sign(PrivateKey privateKey, byte[] message) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM, PROVIDER);
            signature.initSign(privateKey);
            signature.update(message);
            byte[] derSignature = signature.sign();
            // Convert DER format to raw r||s format (64 bytes)
            return convertDerSignatureToRaw(derSignature);
        } catch (Exception e) {
            log.error("SM2 sign failed", e);
            throw new SecurityException("SM2 signature failed", e);
        }
    }

    /**
     * 验证 SM2 签名。
     *
     * @param publicKey SM2 公钥
     * @param message   原始消息
     * @param signature 64字节签名值 (r || s)
     * @return 验签是否通过
     */
    public static boolean verify(PublicKey publicKey, byte[] message, byte[] signature) {
        try {
            // Convert raw signature to DER format for verification
            byte[] derSignature = convertRawSignatureToDer(signature);
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM, PROVIDER);
            sig.initVerify(publicKey);
            sig.update(message);
            return sig.verify(derSignature);
        } catch (Exception e) {
            log.warn("SM2 verify failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 将 DER 编码的签名转换为原始的 r||s 格式。
     */
    private static byte[] convertDerSignatureToRaw(byte[] derSignature) throws IOException {
        org.bouncycastle.asn1.ASN1Sequence seq = 
            org.bouncycastle.asn1.ASN1Sequence.getInstance(derSignature);
        BigInteger r = ((ASN1Integer) seq.getObjectAt(0)).getValue();
        BigInteger s = ((ASN1Integer) seq.getObjectAt(1)).getValue();
        
        byte[] raw = new byte[64];
        byte[] rBytes = trimLeadingZeros(r.toByteArray());
        byte[] sBytes = trimLeadingZeros(s.toByteArray());
        
        System.arraycopy(rBytes, 0, raw, 32 - rBytes.length, rBytes.length);
        System.arraycopy(sBytes, 0, raw, 64 - sBytes.length, sBytes.length);
        return raw;
    }

    /**
     * 将原始的 r||s 格式签名转换为 DER 编码。
     */
    private static byte[] convertRawSignatureToDer(byte[] rawSignature) throws IOException {
        if (rawSignature.length != 64) {
            throw new IllegalArgumentException("Invalid signature length, expected 64 bytes");
        }
        BigInteger r = new BigInteger(1, java.util.Arrays.copyOfRange(rawSignature, 0, 32));
        BigInteger s = new BigInteger(1, java.util.Arrays.copyOfRange(rawSignature, 32, 64));
        return new DERSequence(new ASN1Integer[]{new ASN1Integer(r), new ASN1Integer(s)}).getEncoded();
    }

    /**
     * 去除大整数字节数组前面的零字节。
     */
    private static byte[] trimLeadingZeros(byte[] bytes) {
        int i = 0;
        while (i < bytes.length - 1 && bytes[i] == 0) {
            i++;
        }
        return java.util.Arrays.copyOfRange(bytes, i, bytes.length);
    }

    /**
     * 计算 SM3 哈希值。
     *
     * @param data 输入数据
     * @return 32字节哈希值
     */
    public static byte[] sm3(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SM3", PROVIDER);
            return md.digest(data);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new SecurityException("SM3 not available", e);
        }
    }

    /**
     * 计算 SM3 哈希值（字符串）。
     */
    public static String sm3Hex(String data) {
        return Hex.toHexString(sm3(data.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 计算 SM3 哈希值（字节数组）。
     */
    public static String sm3Hex(byte[] data) {
        return Hex.toHexString(sm3(data));
    }

    /**
     * 从十六进制字符串创建 SM2 公钥。
     *
     * @param hex 十六进制公钥（支持 X.509 编码或原始椭圆曲线点）
     * @return PublicKey
     */
    public static PublicKey decodePublicKey(String hex) throws Exception {
        byte[] keyBytes = Hex.decode(hex);
        return decodePublicKey(keyBytes);
    }

    /**
     * 从字节数组创建 SM2 公钥。
     * 支持 X.509 SubjectPublicKeyInfo 格式和原始椭圆曲线点格式。
     */
    public static PublicKey decodePublicKey(byte[] keyBytes) throws Exception {
        // Check if it's X.509 format (starts with ASN.1 sequence)
        if (keyBytes.length > 2 && keyBytes[0] == 0x30) {
            // X.509 SubjectPublicKeyInfo format - parse and create BCECPublicKey
            X9ECParameters ecParams = GMNamedCurves.getByName("sm2p256v1");
            org.bouncycastle.jce.spec.ECParameterSpec ecSpec = 
                new org.bouncycastle.jce.spec.ECParameterSpec(
                    ecParams.getCurve(),
                    ecParams.getG(),
                    ecParams.getN(),
                    ecParams.getH()
                );
            org.bouncycastle.asn1.x509.SubjectPublicKeyInfo spki = 
                org.bouncycastle.asn1.x509.SubjectPublicKeyInfo.getInstance(keyBytes);
            org.bouncycastle.math.ec.ECPoint bcPoint = 
                ecSpec.getCurve().decodePoint(spki.getPublicKeyData().getBytes());
            return new BCECPublicKey("SM2", 
                new org.bouncycastle.jce.spec.ECPublicKeySpec(bcPoint, ecSpec),
                org.bouncycastle.jce.provider.BouncyCastleProvider.CONFIGURATION);
        }
        
        // Otherwise, treat as raw EC point format
        X9ECParameters ecParams = GMNamedCurves.getByName("sm2p256v1");
        org.bouncycastle.math.ec.ECCurve bcCurve = ecParams.getCurve();
        
        org.bouncycastle.math.ec.ECPoint bcPoint;
        if (keyBytes.length == 65 && keyBytes[0] == 0x04) {
            bcPoint = bcCurve.decodePoint(keyBytes);
        } else if (keyBytes.length == 33) {
            bcPoint = bcCurve.decodePoint(keyBytes);
        } else {
            throw new IllegalArgumentException("Invalid public key format, expected 33 or 65 bytes");
        }
        
        BigInteger p = bcCurve.getField().getCharacteristic();
        BigInteger a = bcCurve.getA().toBigInteger();
        BigInteger b = bcCurve.getB().toBigInteger();
        BigInteger n = ecParams.getN();
        int h = ecParams.getH().intValue();
        
        EllipticCurve javaCurve = new EllipticCurve(
            new java.security.spec.ECFieldFp(p), a, b, null
        );
        ECPoint javaG = new ECPoint(
            ecParams.getG().getAffineXCoord().toBigInteger(),
            ecParams.getG().getAffineYCoord().toBigInteger()
        );
        ECPoint javaPoint = new ECPoint(
            bcPoint.getAffineXCoord().toBigInteger(),
            bcPoint.getAffineYCoord().toBigInteger()
        );
        
        ECParameterSpec javaSpec = new ECParameterSpec(javaCurve, javaG, n, h);
        java.security.spec.ECPublicKeySpec pubKeySpec = 
            new java.security.spec.ECPublicKeySpec(javaPoint, javaSpec);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, PROVIDER);
        return keyFactory.generatePublic(pubKeySpec);
    }

    /**
     * 从十六进制字符串创建 SM2 私钥。
     *
     * @param hex 十六进制私钥（支持 PKCS#8 编码或原始 d 值）
     * @return PrivateKey
     */
    public static PrivateKey decodePrivateKey(String hex) throws Exception {
        byte[] keyBytes = Hex.decode(hex);
        return decodePrivateKey(keyBytes);
    }

    /**
     * 从字节数组创建 SM2 私钥。
     * 支持 PKCS#8 格式和原始私钥 d 值格式。
     */
    public static PrivateKey decodePrivateKey(byte[] keyBytes) throws Exception {
        // Check if it's PKCS#8 format (starts with ASN.1 sequence)
        if (keyBytes.length > 2 && keyBytes[0] == 0x30) {
            // PKCS#8 format - parse and create BCECPrivateKey directly
            X9ECParameters ecParams = GMNamedCurves.getByName("sm2p256v1");
            org.bouncycastle.jce.spec.ECParameterSpec ecSpec = 
                new org.bouncycastle.jce.spec.ECParameterSpec(
                    ecParams.getCurve(),
                    ecParams.getG(),
                    ecParams.getN(),
                    ecParams.getH()
                );
            org.bouncycastle.asn1.pkcs.PrivateKeyInfo pki = 
                org.bouncycastle.asn1.pkcs.PrivateKeyInfo.getInstance(keyBytes);
            org.bouncycastle.asn1.sec.ECPrivateKey ecPrivateKey = 
                org.bouncycastle.asn1.sec.ECPrivateKey.getInstance(pki.parsePrivateKey());
            BigInteger d = ecPrivateKey.getKey();
            return new BCECPrivateKey("SM2", 
                new org.bouncycastle.jce.spec.ECPrivateKeySpec(d, ecSpec),
                org.bouncycastle.jce.provider.BouncyCastleProvider.CONFIGURATION);
        }
        
        // Otherwise, treat as raw private key value (d)
        X9ECParameters ecParams = GMNamedCurves.getByName("sm2p256v1");
        org.bouncycastle.math.ec.ECCurve bcCurve = ecParams.getCurve();
        org.bouncycastle.math.ec.ECPoint bcG = ecParams.getG();
        
        BigInteger p = bcCurve.getField().getCharacteristic();
        BigInteger a = bcCurve.getA().toBigInteger();
        BigInteger b = bcCurve.getB().toBigInteger();
        BigInteger n = ecParams.getN();
        int h = ecParams.getH().intValue();
        
        EllipticCurve javaCurve = new EllipticCurve(
            new java.security.spec.ECFieldFp(p), a, b, null
        );
        
        ECPoint javaG = new ECPoint(
            bcG.getAffineXCoord().toBigInteger(), 
            bcG.getAffineYCoord().toBigInteger()
        );
        
        ECParameterSpec javaSpec = new ECParameterSpec(javaCurve, javaG, n, h);
        BigInteger d = new BigInteger(1, keyBytes);
        java.security.spec.ECPrivateKeySpec privateKeySpec = 
            new java.security.spec.ECPrivateKeySpec(d, javaSpec);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, PROVIDER);
        return keyFactory.generatePrivate(privateKeySpec);
    }

    /**
     * 生成 SM2 密钥对。
     *
     * @return KeyPair
     */
    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(KEY_ALGORITHM, PROVIDER);
            keyGen.initialize(new ECGenParameterSpec("sm2p256v1"), new SecureRandom());
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            throw new SecurityException("Failed to generate SM2 key pair", e);
        }
    }

    /**
     * 生成自签名 SM2 X509 证书。
     *
     * @param keyPair SM2 密钥对
     * @return X509Certificate 自签名证书
     */
    public static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) {
        return generateSelfSignedCertificate(keyPair, "CN=SM2 Self-Signed");
    }

    /**
     * 生成自签名 SM2 X509 证书。
     *
     * @param keyPair       SM2 密钥对
     * @param subjectDN     主题 DN
     * @return X509Certificate 自签名证书
     */
    public static X509Certificate generateSelfSignedCertificate(KeyPair keyPair, String subjectDN) {
        try {
            long now = System.currentTimeMillis();
            Date notBefore = new Date(now - 24 * 60 * 60 * 1000); // 昨天
            Date notAfter = new Date(now + 365L * 24 * 60 * 60 * 1000); // 1年后

            JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                    new X500Name(subjectDN),
                    BigInteger.valueOf(now),
                    notBefore,
                    notAfter,
                    new X500Name(subjectDN),
                    keyPair.getPublic());

            ContentSigner signer = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM)
                    .setProvider(PROVIDER)
                    .build(keyPair.getPrivate());

            return new JcaX509CertificateConverter()
                    .setProvider(PROVIDER)
                    .getCertificate(certBuilder.build(signer));
        } catch (Exception e) {
            throw new SecurityException("Failed to generate self-signed certificate", e);
        }
    }
}
