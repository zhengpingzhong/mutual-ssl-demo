package com.sightcorner.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;


public class Constant {

    /**
     *
     */
    public static final String MESSAGE = "测试文字，用来验证加密和解密情况";


    /**
     * 路径
     */
    //
    private static final String ABSOLUTE_PATH = new File("src/main/resources/").getAbsolutePath();
    //ca
    private static final String CA_ABSOLUTE_PATH = ABSOLUTE_PATH + "/cert/ca/";
    public static final String CA_CERT_PATH = "";
    public static final String CA_KEY_PATH = CA_ABSOLUTE_PATH + "ca.key.pem";
    public static final String CA_KEY_PKCS8_PATH = CA_ABSOLUTE_PATH + "ca.pkcs8.key.pem";
    //intermediate
    private static final String INTERMEDIATE_ABSOLUTE_PATH = ABSOLUTE_PATH + "/cert/intermediate/";
    public static final String INTERMEDIATE_CERT_PATH = INTERMEDIATE_ABSOLUTE_PATH + "intermediate.cert.pem";
    public static final String INTERMEDIATE_KEY_PATH = INTERMEDIATE_ABSOLUTE_PATH + "intermediate.key.pem";
    public static final String INTERMEDIATE_KEY_PASSCODE = "intermediate";
    public static final String INTERMEDIATE_TRUST_STORE_PATH = INTERMEDIATE_ABSOLUTE_PATH + "intermediate.cert.jks";
    //server
    private static final String SERVER_ABSOLUTE_PATH = ABSOLUTE_PATH + "/cert/server/";
    public static final String SERVER_KEY_PATH = SERVER_ABSOLUTE_PATH + "www.example.com.key.pem";
    public static final String SERVER_CERT_PATH = SERVER_ABSOLUTE_PATH + "www.example.com.cert.pem";
    public static final String SERVER_PFX_PATH = SERVER_ABSOLUTE_PATH + "www.example.com.p12";
    public static final String SERVER_KEYSTORE_ALIAS = "1";
    public static final String SERVER_KEYSTORE_PASSWORD = "p12";
    //terminal
    //...

    /**
     * 格式
     */
    public static final String CERTIFICATE_TYPE = "x.509";
    public static final String PFX_TYPE = "PKCS12";
    public static final String JKS_TYPE = "JKS";


    /**
     *
     */
    public static final BouncyCastleProvider BOUNCY_CASTLE_PROVIDER = new BouncyCastleProvider();

}
