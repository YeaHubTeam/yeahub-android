package ru.yeahub.datastore_impl.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Реализация шифрования через Android Keystore:
 * - использует AES/GCM
 * - ключ хранится в Android Keystore
 */
class CryptoManagerImpl : CryptoManager {

    override fun encrypt(value: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())

        val encryptedBytes = cipher.doFinal(
            value.toByteArray(StandardCharsets.UTF_8),
        )

        val combined = cipher.iv + encryptedBytes

        return Base64.encodeToString(
            combined,
            Base64.NO_WRAP,
        )
    }

    override fun decrypt(value: String): String {
        val decoded = Base64.decode(
            value,
            Base64.NO_WRAP,
        )

        val iv = decoded.copyOfRange(
            fromIndex = 0,
            toIndex = IV_SIZE,
        )

        val encryptedBytes = decoded.copyOfRange(
            fromIndex = IV_SIZE,
            toIndex = decoded.size,
        )

        val cipher = Cipher.getInstance(TRANSFORMATION)

        cipher.init(
            Cipher.DECRYPT_MODE,
            getSecretKey(),
            GCMParameterSpec(
                TAG_LENGTH,
                iv,
            ),
        )

        return String(
            cipher.doFinal(encryptedBytes),
            StandardCharsets.UTF_8,
        )
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
            load(null)
        }

        keyStore.getKey(KEY_ALIAS, null)?.let {
            return it as SecretKey
        }

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE,
        )

        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or
                        KeyProperties.PURPOSE_DECRYPT,
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build(),
        )

        return keyGenerator.generateKey()
    }

    private companion object {
        private const val KEY_ALIAS = "auth_token_key"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"

        private const val IV_SIZE = 12
        private const val TAG_LENGTH = 128
    }
}