package com.sightcorner.scenario;


import com.sightcorner.util.Constant;
import com.sightcorner.util.Helper;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import sun.misc.BASE64Encoder;


import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class VerifySignature {

    public static void main(String[] args) throws Exception{


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


        byte[] data = Constant.MESSAGE.getBytes("UTF-8");

        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initSign(privateKey);
        signature.update(data);

        byte[] signatureBytes = signature.sign();

        //Base64并不是一种用于安全领域的加密解密算法（这类算法有DES等），
        //尽管我们有时也听到使用Base64来加密解密的说法，但这里所说的加密与解密实际是指编码（encode）和解码（decode）的过程，
        //其变换是非常简单的，仅仅能够避免信息被直接识别。
        System.out.println("Original Signature: " + new String(signatureBytes));
        for(byte b : signatureBytes) {
            System.out.print(b);
        }
        System.out.println();
        System.out.println("BASE64 Signature: " + new BASE64Encoder().encode(signatureBytes));


        signature.initVerify(publicKey);
        signature.update(data);

        boolean verifyResult = signature.verify(signatureBytes);

        System.out.println("验证签名结果: " + verifyResult);
    }


}
