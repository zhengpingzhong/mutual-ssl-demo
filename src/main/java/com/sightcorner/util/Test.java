package com.sightcorner.util;

import com.sightcorner.util.Constant;

import java.security.*;
import java.security.cert.*;
import java.util.HashSet;
import java.util.Set;


public class Test {

    public static boolean isSelfSigned(X509Certificate cert) throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException {
        try {
            PublicKey key = cert.getPublicKey();cert.verify(key);
            return true;
        } catch (SignatureException sigEx) {
            return false;
        } catch (InvalidKeyException keyEx) {
            return false;
        }
    }

    private static PKIXCertPathBuilderResult verify(X509Certificate target, X509Certificate ca, X509Certificate intermediate) throws Exception {
        Set<X509Certificate> cas = new HashSet<>();
        cas.add(ca);

        Set<X509Certificate> intermediates = new HashSet<>();
        intermediates.add(intermediate);

        return verify(target, cas, intermediates);
    }

    /**
     *
     * @param x509Certificate
     * @param trustRootCertificates
     * @param intermediateCertificate
     * @return
     */
    private static PKIXCertPathBuilderResult verify(X509Certificate x509Certificate, Set<X509Certificate> trustRootCertificates, Set<X509Certificate> intermediateCertificate) throws Exception {
        //
        X509CertSelector x509CertSelector = new X509CertSelector();
        x509CertSelector.setCertificate(x509Certificate);
        //
        Set<TrustAnchor> trustAnchors = new HashSet<>();
        for(X509Certificate trustRootCertificate: trustRootCertificates) {
            trustAnchors.add(new TrustAnchor(trustRootCertificate, null));
        }
        //
        PKIXBuilderParameters pkixBuilderParameters = new PKIXBuilderParameters(trustAnchors, x509CertSelector);
        pkixBuilderParameters.setRevocationEnabled(false);
        //
        CertStore intermediateCertStore = CertStore.getInstance(
                "Collection",
                new CollectionCertStoreParameters(intermediateCertificate),
                Constant.BOUNCY_CASTLE_PROVIDER);
        pkixBuilderParameters.addCertStore(intermediateCertStore);
        //
        CertPathBuilder certPathBuilder = CertPathBuilder.getInstance("PKIX", Constant.BOUNCY_CASTLE_PROVIDER);
        PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult) certPathBuilder.build(pkixBuilderParameters);

        return result;
    }
}
