package com.sightcorner.util;


import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class Helper {
    private static final String CERTIFICATE_TYPE = "x.509";
    private static final String PFX_TYPE = "PKCS12";
    private static final String JKS = "JKS";



    /**
     * 获取 公钥
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKeyFromPath(String certificatePath) throws Exception {

        Certificate certificate = getCertificateFromPath(certificatePath);
        PublicKey publicKey = certificate.getPublicKey();
        return publicKey;
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
     * 获取 证书
     * @param certificatePath
     * @return
     */
    private static Certificate getCertificateFromPath(String certificatePath) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(CERTIFICATE_TYPE);
        FileInputStream fileInputStream = new FileInputStream(certificatePath);
        Certificate certificate = certificateFactory.generateCertificate(fileInputStream);
        fileInputStream.close();
        return certificate;
    }



    /**
     * 获取 密钥库
     * @param pfxPath
     * @param password
     * @return
     */
    private static KeyStore getKeyStoreFromPFXPath(String pfxPath, String password) throws Exception{
        KeyStore keyStore = KeyStore.getInstance(PFX_TYPE);
        FileInputStream fileInputStream = new FileInputStream(pfxPath);
        keyStore.load(fileInputStream, password.toCharArray());
        fileInputStream.close();
        return keyStore;
    }

}
