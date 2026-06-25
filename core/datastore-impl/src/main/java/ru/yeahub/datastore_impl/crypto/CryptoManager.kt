package ru.yeahub.datastore_impl.crypto

/**
 * Контракт шифрования данных:
 * - encrypt - шифрует строку
 * - decrypt - расшифровывает строку
 */
interface CryptoManager {

    fun encrypt(value: String): String

    fun decrypt(value: String): String
}