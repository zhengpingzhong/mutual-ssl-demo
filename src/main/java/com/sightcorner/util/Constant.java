package com.sightcorner.util;

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
    public static final String ABSOLUTE_PATH = new File("src/main/resources/").getAbsolutePath();
    //ca
    public static final String CA_ABSOLUTE_PATH = ABSOLUTE_PATH + "/cert/ca/";
    //intermediate
    public static final String INTERMEDIATE_ABSOLUTE_PATH = ABSOLUTE_PATH + "/cert/intermediate/";
    public static final String INTERMEDIATE_PUBLIC_KEY_PATH = INTERMEDIATE_ABSOLUTE_PATH + "intermediate.cert.pem";
    public static final String INTERMEDIATE_TRUST_STORE_PATH = INTERMEDIATE_ABSOLUTE_PATH + "intermediate.cert.jks";
    //server
    public static final String SERVER_ABSOLUTE_PATH = ABSOLUTE_PATH + "/cert/server/";
    public static final String SERVER_PUBLIC_KEY_PATH = SERVER_ABSOLUTE_PATH + "www.example.com.key.pem";
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

}
