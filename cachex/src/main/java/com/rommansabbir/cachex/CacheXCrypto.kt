package com.rommansabbir.cachex

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.security.GeneralSecurityException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


object CacheXCrypto {
    private const val ITERATION_COUNT = 1000
    private const val KEY_LENGTH = 256
    private const val PBKDF2_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1"
    private const val CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"
    private const val PKCS5_SALT_LENGTH = 32
    private const val DELIMITER = "]"
    private val random = SecureRandom()

    /**
     * Encrypt a given string with AES encryption's
     *
     * @param dataToEncrypt, data that need to be encrypted in [String] format
     *
     * @return [String], it will return a encrypted data in [String] format or throw [RuntimeException]
     */
    fun encrypt(dataToEncrypt: String): String {
        val salt = generateSalt()
        val key = deriveKey(CacheX.getKey(), salt)

        try {
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            val iv = generateIv(cipher.blockSize)
            val ivParams = IvParameterSpec(iv)
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams)
            val cipherText = cipher.doFinal(dataToEncrypt.toByteArray(Charsets.UTF_8))

            return if (salt != null) {
                String.format(
                    "%s%s%s%s%s",
                    toBase64(salt),
                    DELIMITER,
                    toBase64(iv),
                    DELIMITER,
                    toBase64(cipherText)
                )
            } else String.format(
                "%s%s%s",
                toBase64(iv),
                DELIMITER,
                toBase64(cipherText)
            )

        } catch (e: GeneralSecurityException) {
            throw RuntimeException(e)
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException(e)
        }

    }

    /**
     * Decrypt a given string with AES decryption's
     *
     * @param dataToDecrypt, data that need to be decrypted in [String] format
     *
     * @return [String], it will return a decrypted data in [String] format or throw [RuntimeException]
     */
    fun decrypt(dataToDecrypt: String): String {
        val fields =
            dataToDecrypt.split(DELIMITER.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        require(fields.size == 3) { "Invalid encypted text format" }
        val salt = fromBase64(fields[0])
        val iv = fromBase64(fields[1])
        val cipherBytes = fromBase64(fields[2])
        val key = deriveKey(CacheX.getKey(), salt)

        try {
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            val ivParams = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams)
            val plaintext = cipher.doFinal(cipherBytes)
            return String(plaintext, Charsets.UTF_8)
        } catch (e: GeneralSecurityException) {
            throw RuntimeException(e)
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException(e)
        }
    }

    /**
     * Parse data to [ByteArray] type
     *
     * @return [ByteArray]
     */
    private fun generateSalt(): ByteArray? {
        val b = ByteArray(PKCS5_SALT_LENGTH)
        random.nextBytes(b)
        return b
    }

    /**
     * Generate IV
     *
     * @param length, [Int]
     *
     * @return [ByteArray]
     */
    private fun generateIv(length: Int): ByteArray {
        val b = ByteArray(length)
        random.nextBytes(b)
        return b
    }

    /**
     * Generate secret key from password & salted data
     *
     * @param password, password to generate secret key in [String] format
     * @param salt, salted data in [ByteArray] format for secret key
     *
     * @return [SecretKey], it will return a secret key or [RuntimeException]
     */
    private fun deriveKey(password: String, salt: ByteArray?): SecretKey {
        try {
            val keySpec = PBEKeySpec(password.toCharArray(), salt!!, ITERATION_COUNT, KEY_LENGTH)
            val keyFactory = SecretKeyFactory.getInstance(PBKDF2_DERIVATION_ALGORITHM)
            val keyBytes = keyFactory.generateSecret(keySpec).encoded
            return SecretKeySpec(keyBytes, "AES")
        } catch (e: GeneralSecurityException) {
            throw RuntimeException(e)
        }
    }

    /**
     * Convert a given [ByteArray] to [String] format
     *
     * @param bytes, [ByteArray] data that need to be decoded
     *
     * @return [String], decoded data will be returned into [String] format
     */
    private fun toBase64(bytes: ByteArray): String {
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    /**
     * Convert a given [String] to [ByteArray] format
     *
     * @param base64, [String] data that need to be decoded
     *
     * @return [ByteArray], decoded data will be returned into [ByteArray] format
     */
    private fun fromBase64(base64: String): ByteArray {
        return Base64.decode(base64, Base64.NO_WRAP)
    }

}