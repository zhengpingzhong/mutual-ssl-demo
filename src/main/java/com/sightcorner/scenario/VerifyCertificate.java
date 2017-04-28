package com.sightcorner.scenario;


import com.sightcorner.util.Constant;
import com.sightcorner.util.Helper;

import java.security.Signature;
import java.security.cert.X509Certificate;

public class VerifyCertificate {


    public static void main(String[] args) throws Exception {

        X509Certificate caCertificate = Helper.getCertificateFromCertificatePath(Constant.SERVER_CERT_PATH);
        X509Certificate intermediateCertificate = Helper.getCertificateFromCertificatePath(Constant.INTERMEDIATE_CERT_PATH);
        X509Certificate serverCertificate = Helper.getCertificateFromCertificatePath(Constant.SERVER_CERT_PATH);

        /**
         * 验证“Intermediate数字证书”是否由“CA”签发
         */
        caCertificate.verify(intermediateCertificate.getPublicKey());

        /**
         * 验证“Server数字证书”是否由“Intermediate”签发
         */
//        intermediateCertificate.verify(serverCertificate.getPublicKey());


        /**
         * 验证“Server数字证书”是否由“CA”签发
         */
        caCertificate.verify(serverCertificate.getPublicKey());
    }


}
