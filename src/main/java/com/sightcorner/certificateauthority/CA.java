package com.sightcorner.certificateauthority;


import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class CA {

    private static final String CERTIFICATE_TYPE = "x.509";
    private static final String MESSAGE = "测试文字，用来验证加密和解密情况";
    private static final String CERTIFICATE_PATH = "";
    private static final String KEYSTORE_PATH = "";
    private static final String KEYSTORE_PASSWORD = "";
    private static final String KEYSTORE_ALIAS = "";

    /**
     * 获取 公钥
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKey() throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(CERTIFICATE_TYPE);
        FileInputStream fileInputStream = new FileInputStream(CERTIFICATE_PATH);
        Certificate certificate = certificateFactory.generateCertificate(fileInputStream);
        fileInputStream.close();
        PublicKey publicKey = certificate.getPublicKey();
        return publicKey;
    }

    /**
     * 获取 私钥
     * @return
     * @throws Exception
     */
    private static PrivateKey getPrivateKey() throws Exception {

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream fileInputStream = new FileInputStream(KEYSTORE_PATH);
        keyStore.load(fileInputStream, KEYSTORE_PASSWORD.toCharArray());
        fileInputStream.close();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEYSTORE_ALIAS, KEYSTORE_PASSWORD.toCharArray());
        return privateKey;
    }


    private static byte[] encryptOrDecrypt(int mode, byte[] data, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        cipher.init(mode, key);
        byte[] result = cipher.doFinal(data);
        return result;
    }

    private static void output(byte[]... bytes) {
        for(byte[] b : bytes) {
            System.out.println(b);
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {

        //1. 公钥加密 私钥解密
        byte[] b1 = MESSAGE.getBytes();
        byte[] b2 = encryptOrDecrypt(Cipher.ENCRYPT_MODE, b1, getPublicKey());
        byte[] b3 = encryptOrDecrypt(Cipher.DECRYPT_MODE, b2, getPrivateKey());
        output(b1, b2, b3);

        //2. 私钥加密 公钥解密
    }
}
