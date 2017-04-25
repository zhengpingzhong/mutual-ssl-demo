package com.sightcorner.util;


import java.io.FileInputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class Helper {

    /**
     * 获取 证书
     * @param certificatePath
     * @return
     * @throws Exception
     */
    public static Certificate getCertificateFromCertificatePath(String certificatePath) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(Constant.CERTIFICATE_TYPE);
        FileInputStream fileInputStream = new FileInputStream(certificatePath);
        Certificate certificate = certificateFactory.generateCertificate(fileInputStream);
        fileInputStream.close();
        return certificate;
    }


    /**
     * 获取 公钥
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKeyFromCertificatePath(String certificatePath) throws Exception {

        Certificate certificate = getCertificateFromCertificatePath(certificatePath);
        PublicKey publicKey = certificate.getPublicKey();
        return publicKey;
    }


    /**
     *
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     * @throws Exception
     */
    public static Certificate getCertificateFromKeyStorePath(String keyStorePath, String alias, String password) throws Exception {
        KeyStore keyStore = getKeyStoreFromPFXPath(keyStorePath, password);
        Certificate certificate = keyStore.getCertificate(alias);
        return certificate;
    }


    /**
     * 获取 私钥
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromPFXPath(String pfxPath, String alias, String password) throws Exception {

        KeyStore keyStore = getKeyStoreFromPFXPath(pfxPath, password);
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
        return privateKey;
    }



    //////////////////////////////////////////////////////////
    // 以下为私有方法，不可向外调用
    //////////////////////////////////////////////////////////


    /**
     * 获取 密钥库
     * @param pfxPath
     * @param password
     * @return
     */
    private static KeyStore getKeyStoreFromPFXPath(String pfxPath, String password) throws Exception{
        KeyStore keyStore = KeyStore.getInstance(Constant.PFX_TYPE);
        FileInputStream fileInputStream = new FileInputStream(pfxPath);
        keyStore.load(fileInputStream, password.toCharArray());
        fileInputStream.close();
        return keyStore;
    }

}
