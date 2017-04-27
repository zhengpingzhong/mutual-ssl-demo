package com.sightcorner.scenario;


import com.sightcorner.util.Constant;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
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
import org.bouncycastle.util.io.pem.PemReader;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.TreeSet;

public class EncryptAndDecrypt {




    /**
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        EncryptAndDecrypt instance = new EncryptAndDecrypt();
        Object object = instance.getPEMObject(Constant.CA_KEY_PATH);
        instance.parsePEMObject(object);
    }


    public Object getPEMObject(String filePath) throws Exception {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        Reader reader = null;
        Object object = null;
        try {
            inputStream = new FileInputStream(new File(filePath));
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            PEMParser pemParser = new PEMParser(reader);
            object = pemParser.readObject();
            System.out.println(pemParser.readPemObject());
        }finally {
            inputStream.close();
            inputStreamReader.close();
            reader.close();
        }
        return object;
    }

    public void parsePEMObject(Object object) throws Exception {
        System.out.println(object);
        if(object instanceof X509CertificateHolder) {
            this.handleX509CertificateHolder((X509CertificateHolder) object);
        } else if(object instanceof PEMEncryptedKeyPair) {
            this.handlePEMEncryptedKeyPair((PEMEncryptedKeyPair) object);
        } else if(object instanceof PKCS8EncryptedPrivateKeyInfo) {
            this.handlePKCS8EncryptedPrivateKeyInfo((PKCS8EncryptedPrivateKeyInfo) object);
        } else {
            this.handleEles();
        }
    }

    private void handleEles() {
        System.out.println("handleEles");
    }

    private void handlePKCS8EncryptedPrivateKeyInfo(PKCS8EncryptedPrivateKeyInfo pkcs8EncryptedPrivateKeyInfo) throws OperatorCreationException, PKCSException, PEMException {
        System.out.println("handlePKCS8EncryptedPrivateKeyInfo");

        InputDecryptorProvider passcode = new JceOpenSSLPKCS8DecryptorProviderBuilder().build("root".toCharArray());
        PrivateKeyInfo privateKeyInfo = pkcs8EncryptedPrivateKeyInfo.decryptPrivateKeyInfo(passcode);

        JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter().setProvider(Constant.BOUNCY_CASTLE_PROVIDER);
        BCRSAPrivateCrtKey bcrsaPrivateCrtKey = (BCRSAPrivateCrtKey) jcaPEMKeyConverter.getPrivateKey(privateKeyInfo);

        System.out.println(bcrsaPrivateCrtKey);
    }

    private void handlePEMEncryptedKeyPair(PEMEncryptedKeyPair pmeEncryptedKeyPair) throws IOException, OperatorCreationException {
        System.out.println("handlePEMEncryptedKeyPair");

        PEMDecryptorProvider pemDecryptorProvider = new JcePEMDecryptorProviderBuilder().build("root".toCharArray());
        PEMKeyPair pemKeyPair = pmeEncryptedKeyPair.decryptKeyPair(pemDecryptorProvider);
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
     * 1. 公钥加密 私钥解密
     */

    /**
     * 2. 私钥加密 公钥解密
     */


//        PublicKey publicKey = Helper.getPublicKeyFromCertificatePath(Constant.SERVER_CERT_PATH);
//        PrivateKey privateKey = Helper.getPrivateKeyFromPFXPath(Constant.SERVER_PFX_PATH, Constant.SERVER_KEYSTORE_ALIAS, Constant.SERVER_KEYSTORE_PASSWORD);
//        encryptAndDecrypt(publicKey, privateKey);
//        encryptAndDecrypt(privateKey, publicKey);

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
