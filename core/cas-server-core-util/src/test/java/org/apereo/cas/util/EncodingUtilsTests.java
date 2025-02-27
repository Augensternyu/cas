package org.apereo.cas.util;

import org.apereo.cas.util.crypto.DecryptionException;
import org.apereo.cas.util.crypto.PrivateKeyFactoryBean;
import org.apereo.cas.util.crypto.PublicKeyFactoryBean;

import lombok.val;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.keys.AesKey;
import org.jose4j.keys.RsaKeyUtil;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link EncodingUtilsTests}.
 *
 * @author Misagh Moayyed
 * @since 5.2.0
 */
@Tag("Utility")
class EncodingUtilsTests {

    private static PrivateKey getPrivateKey() throws Exception {
        val factory = new PrivateKeyFactoryBean();
        factory.setAlgorithm(RsaKeyUtil.RSA);
        factory.setLocation(new ClassPathResource("keys/RSA2048Private.key"));
        factory.setSingleton(false);
        assertSame(PrivateKey.class, factory.getObjectType());
        return factory.getObject();
    }
    
    private static PublicKey getPublicKey() throws Exception {
        val factory = new PublicKeyFactoryBean(new ClassPathResource("keys/RSA2048Public.key"), RsaKeyUtil.RSA);
        factory.setSingleton(false);
        assertSame(PublicKey.class, factory.getObjectType());
        return factory.getObject();
    }

    @Test
    void verifyAesKeyForJwtSigning() {
        val secret = EncodingUtils.generateJsonWebKey(512);
        val key = new AesKey(secret.getBytes(StandardCharsets.UTF_8));
        val value = "ThisValue";
        val signed = EncodingUtils.signJwsHMACSha512(key, value.getBytes(StandardCharsets.UTF_8), Map.of());
        val jwt = EncodingUtils.verifyJwsSignature(key, signed);
        val result = new String(jwt, StandardCharsets.UTF_8);
        assertEquals(value, result);
    }

    @Test
    void verifyRsaKeyForJwtSigning() throws Throwable {
        val value = "ThisValue";
        val signed = EncodingUtils.signJwsRSASha512(getPrivateKey(), value.getBytes(StandardCharsets.UTF_8), Map.of());
        val jwt = EncodingUtils.verifyJwsSignature(getPublicKey(), signed);
        val result = new String(jwt, StandardCharsets.UTF_8);
        assertEquals(value, result);
    }

    @Test
    void verifyAesKeyForJwtEncryption() {
        val secret = EncodingUtils.generateJsonWebKey(256);
        val key = EncodingUtils.generateJsonWebKey(secret);
        val value = "ThisValue";
        val found = EncodingUtils.encryptValueAsJwtDirectAes128Sha256(key, value);
        val jwt = EncodingUtils.decryptJwtValue(key, found);
        assertEquals(value, jwt);

        assertThrows(DecryptionException.class, () -> EncodingUtils.decryptJwtValue(key, null));
    }

    @Test
    void verifyRsaKeyForJwtEncryption() throws Throwable {
        val value = "ThisValue";
        val found = EncodingUtils.encryptValueAsJwtRsaOeap256Aes256Sha512(getPublicKey(), value);
        val jwt = EncodingUtils.decryptJwtValue(getPrivateKey(), found);
        assertEquals(value, jwt);
    }

    @Test
    void verifyHex() {
        assertNull(EncodingUtils.hexDecode("one"));
        assertNull(EncodingUtils.hexDecode(StringUtils.EMPTY));
        assertNull(EncodingUtils.hexEncode((byte[]) null));
        assertNull(EncodingUtils.hexEncode((String) null));
        assertNull(EncodingUtils.hexDecode("one".toCharArray()));
        assertNull(EncodingUtils.hexDecode((char[]) null));
    }

    @Test
    void verifyEncoding() {
        assertNull(EncodingUtils.urlDecode(null));

        assertTrue(EncodingUtils.encodeBase64(ArrayUtils.EMPTY_BYTE_ARRAY, true).isEmpty());
        assertFalse(EncodingUtils.encodeBase64("one".getBytes(StandardCharsets.UTF_8), true).isEmpty());

        assertFalse(EncodingUtils.encodeBase32("one".getBytes(StandardCharsets.UTF_8), true).isEmpty());
        assertFalse(EncodingUtils.encodeBase32("one".getBytes(StandardCharsets.UTF_8), false).isEmpty());
        assertFalse(EncodingUtils.encodeBase64("one".getBytes(StandardCharsets.UTF_8), false).isEmpty());
    }
}
