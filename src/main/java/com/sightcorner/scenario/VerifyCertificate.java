package com.sightcorner.scenario;


import com.sightcorner.util.Constant;
import com.sightcorner.util.Helper;

import java.security.cert.*;

public class VerifyCertificate {


    public static void main(String[] args) throws Exception {

        X509Certificate caCertificate = Helper.getCertificateFromCertificatePath(Constant.CA_CERT_PATH);
        X509Certificate intermediateCertificate = Helper.getCertificateFromCertificatePath(Constant.INTERMEDIATE_CERT_PATH);
        X509Certificate serverCertificate = Helper.getCertificateFromCertificatePath(Constant.SERVER_CERT_PATH);


        /**
         * 验证 CA 的数字证书 是由 CA 私钥进行签名的
         */
        caCertificate.verify(caCertificate.getPublicKey());


        /**
         * 验证 Intermediate 的数字证书 是由 CA 私钥进行签名的
         */
        intermediateCertificate.verify(caCertificate.getPublicKey());


        /**
         * 验证 Server 的数字证书 是由 Intermediate 进行签名的
         */
        serverCertificate.verify(intermediateCertificate.getPublicKey());


        /**
         * 验证 Server 的数字证书 是由 CA 进行签名的
         */
        if(serverCertificate.getIssuerDN().equals(intermediateCertificate.getSubjectDN())) {
            serverCertificate.verify(intermediateCertificate.getPublicKey());

            if(intermediateCertificate.getIssuerDN().equals(caCertificate.getSubjectDN())) {
                intermediateCertificate.verify(caCertificate.getPublicKey());

                if(caCertificate.getIssuerDN().equals(caCertificate.getSubjectDN())) {
                    caCertificate.verify(caCertificate.getPublicKey());

                    System.out.println("溯源签名认证");
                }
            }
        }


    }




}
