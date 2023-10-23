package util

import java.math.BigInteger
import java.security.KeyFactory
import java.security.KeyPair
import java.security.PublicKey
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPrivateKeySpec
import java.security.spec.RSAPublicKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

private val rsaKeyFactory = KeyFactory.getInstance("RSA")

/**
 * 目前没有找到使用 pkcs1 的，只能用 pkcs8 的 pem 格式
 * -----BEGIN PRIVATE KEY----- 开头
 */
@OptIn(ExperimentalEncodingApi::class)
fun readPrivateKey(key: String): RSAPrivateKey {
    val text = key.replace("-----BEGIN PRIVATE KEY-----", "")
        .replace(System.lineSeparator(), "")
        .replace("-----END PRIVATE KEY-----", "")
        .trimIndent()
    val bytes = Base64.decode(text)
    val spec = PKCS8EncodedKeySpec(bytes)
    return rsaKeyFactory.generatePrivate(spec) as RSAPrivateKey
}

val RSAPrivateKey.publicKey: PublicKey get() = run {
    val priSpec = rsaKeyFactory.getKeySpec(this, RSAPrivateKeySpec::class.java)
    val pubSpec = RSAPublicKeySpec(priSpec.modulus, BigInteger.valueOf(65537))
    rsaKeyFactory.generatePublic(pubSpec)
}

val RSAPrivateKey.keyPair: KeyPair get() = run {
    KeyPair(publicKey, this)
}