package com.sightcorner.scenario;


import com.sightcorner.util.Constant;
import com.sightcorner.util.Helper;

import javax.crypto.Cipher;
import java.security.*;

public class EncryptAndDecrypt {




    /**
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {

        PublicKey publicKey = Helper.getPublicKeyFromCertificatePath(Constant.SERVER_CERT_PATH);
        PrivateKey privateKey = Helper.getPrivateKeyFromPFXPath(Constant.SERVER_PFX_PATH, Constant.SERVER_KEYSTORE_ALIAS, Constant.SERVER_KEYSTORE_PASSWORD);

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
