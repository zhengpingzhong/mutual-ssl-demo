package com.sightcorner.util;


import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateCrtKey;
import org.bouncycastle.openssl.*;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class Helper {



    public static Object getPEMObject(String filePath) throws Exception {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        Reader reader = null;
        Object object = null;
        try {
            inputStream = new FileInputStream(new File(filePath));
            inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStreamReader);
            PEMParser pemParser = new PEMParser(reader);
            object = pemParser.readObject();
        }finally {
            inputStream.close();
            inputStreamReader.close();
            reader.close();
        }
        return object;
    }

    /**
     * 解析 PEM 对象
     * @param object
     * @throws Exception
     */
    public void parsePEMObject(Object object) throws Exception {
        System.out.println(object);
        if(object instanceof X509CertificateHolder) {
            this.handleX509CertificateHolder((X509CertificateHolder) object);
        } else if(object instanceof PEMEncryptedKeyPair) {
            String passPhrase = "";
            this.handlePEMEncryptedKeyPair((PEMEncryptedKeyPair) object, passPhrase);
        } else if(object instanceof PKCS8EncryptedPrivateKeyInfo) {
            this.handlePKCS8EncryptedPrivateKeyInfo((PKCS8EncryptedPrivateKeyInfo) object);
        } else if(object instanceof PEMKeyPair) {
            this.handlePEMKeyPair((PEMKeyPair) object);
        } else {
            this.handleEles();
        }
    }

    private void handlePEMKeyPair(PEMKeyPair pemKeyPair) {

        System.out.println(pemKeyPair.getPrivateKeyInfo());
        System.out.println(pemKeyPair.getPublicKeyInfo());
    }

    private void handleEles() {
        System.out.println("handleEles");
    }

    private void handlePKCS8EncryptedPrivateKeyInfo(PKCS8EncryptedPrivateKeyInfo pkcs8EncryptedPrivateKeyInfo) throws OperatorCreationException, PKCSException, PEMException {
        System.out.println("handlePKCS8EncryptedPrivateKeyInfo");

        InputDecryptorProvider inputDecryptorProvider = new JceOpenSSLPKCS8DecryptorProviderBuilder().build("root".toCharArray());
        PrivateKeyInfo privateKeyInfo = pkcs8EncryptedPrivateKeyInfo.decryptPrivateKeyInfo(inputDecryptorProvider);

        JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter().setProvider(Constant.BOUNCY_CASTLE_PROVIDER);
        BCRSAPrivateCrtKey bcrsaPrivateCrtKey = (BCRSAPrivateCrtKey) jcaPEMKeyConverter.getPrivateKey(privateKeyInfo);

        System.out.println(bcrsaPrivateCrtKey);
    }

    private void handlePEMEncryptedKeyPair(PEMEncryptedKeyPair pemEncryptedKeyPair, String passPhrase) throws Exception {
        System.out.println("handlePEMEncryptedKeyPair");

        PEMDecryptorProvider pemDecryptorProvider = new JcePEMDecryptorProviderBuilder().build(passPhrase.toCharArray());
        PEMKeyPair pemKeyPair = pemEncryptedKeyPair.decryptKeyPair(pemDecryptorProvider);
        PrivateKeyInfo privateKeyInfo = pemKeyPair.getPrivateKeyInfo();

        System.out.println("privateKeyInfo");
        System.out.println(privateKeyInfo);

        JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter().setProvider(Constant.BOUNCY_CASTLE_PROVIDER);
        PrivateKey privateKey = jcaPEMKeyConverter.getPrivateKey(privateKeyInfo);

        System.out.println("privateKey");
        System.out.println(privateKey);
    }

    private void handleX509CertificateHolder(X509CertificateHolder holder) throws CertificateException {
        System.out.println("handleX509CertificateHolder");
        X509Certificate certificate = new JcaX509CertificateConverter().setProvider(Constant.BOUNCY_CASTLE_PROVIDER).getCertificate(holder);
        System.out.println(certificate);
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
     * 获取 私钥
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromPFXPath(String pfxPath, String alias, String password) throws Exception {

        KeyStore keyStore = getKeyStoreFromPFXPath(pfxPath, password);
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
        return privateKey;
    }


    /**
     *
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     * @throws Exception
     */
    private static Certificate getCertificateFromKeyStorePath(String keyStorePath, String alias, String password) throws Exception {
        KeyStore keyStore = getKeyStoreFromPFXPath(keyStorePath, password);
        Certificate certificate = keyStore.getCertificate(alias);
        return certificate;
    }


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


    /**
     * 获取 证书
     * @param certificatePath
     * @return
     * @throws Exception
     */
    private static Certificate getCertificateFromCertificatePath(String certificatePath) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(Constant.CERTIFICATE_TYPE);
        FileInputStream fileInputStream = new FileInputStream(certificatePath);
        Certificate certificate = certificateFactory.generateCertificate(fileInputStream);
        fileInputStream.close();
        return certificate;
    }
}
