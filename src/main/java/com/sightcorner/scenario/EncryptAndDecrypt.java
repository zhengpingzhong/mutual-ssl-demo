package com.sightcorner.scenario;


import com.sightcorner.util.Constant;
import com.sightcorner.util.Helper;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateCrtKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.*;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.*;

public class EncryptAndDecrypt {

    /**
     * 通过原始的“加密”的“密钥对文件”对信息进行“公钥加密，私钥解密”和“私钥加密，公钥解密”的验证
     * @param args
     */
    public static void main(String[] args) throws Exception {
        /**
         * 0. 初始
         */
        //解读原始的 CA_KEY 文件
        PEMEncryptedKeyPair pemEncryptedKeyPair = (PEMEncryptedKeyPair) Helper.getPEMObject(Constant.CA_KEY_PATH);
        //用“通行码”打开密钥对文件
        PEMDecryptorProvider pemDecryptorProvider = new JcePEMDecryptorProviderBuilder().build(Constant.CA_KEY_PASSPHRASE.toCharArray());
        PEMKeyPair pemKeyPair = pemEncryptedKeyPair.decryptKeyPair(pemDecryptorProvider);
        //获取私钥和公钥的基本信息
        PrivateKeyInfo privateKeyInfo = pemKeyPair.getPrivateKeyInfo();
        SubjectPublicKeyInfo subjectPublicKeyInfo = pemKeyPair.getPublicKeyInfo();
        //用私钥基本信息生成私钥，用公钥基本信息生成公钥
        JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter().setProvider(Constant.BOUNCY_CASTLE_PROVIDER);
        PrivateKey privateKey = jcaPEMKeyConverter.getPrivateKey(privateKeyInfo);
        PublicKey publicKey = jcaPEMKeyConverter.getPublicKey(subjectPublicKeyInfo);

        /**
         * 1. 公钥加密 私钥解密
         */
        encryptAndDecrypt(publicKey, privateKey);

        /**
         * 2. 私钥加密 公钥解密
         */
        encryptAndDecrypt(privateKey, publicKey);
    }





    private static void encryptAndDecrypt(Key encryptKey, Key decryptKey) throws Exception{
        byte[] b1 = Constant.MESSAGE.getBytes();
        byte[] b2 = encryptOrDecrypt(Cipher.ENCRYPT_MODE, b1, encryptKey);
        byte[] b3 = encryptOrDecrypt(Cipher.DECRYPT_MODE, b2, decryptKey);
        output(b1, b2, b3);
    }

    private static byte[] encryptOrDecrypt(int mode, byte[] data, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        cipher.init(mode, key);
        byte[] result = cipher.doFinal(data);
        return result;
    }

    private static void output(byte[]... bytes) {
        for(byte[] b : bytes) {
            System.out.println(new String(b));
        }
    }

}
