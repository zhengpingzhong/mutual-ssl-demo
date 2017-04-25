package com.sightcorner.scenario;


import com.sightcorner.util.Constant;
import com.sightcorner.util.Helper;

import java.security.Signature;
import java.security.cert.X509Certificate;

public class VerifyCertificate {


    public static void main(String[] args) throws Exception {
        byte[] data = Constant.MESSAGE.getBytes();
        byte[] sign = signData(data);

        /**
         * 1. 使用 CA 私钥 进行签名
         */



        /**
         * 2. 使用 CA 数字证书 验证
         */
    }

    private static byte[] signData(byte[] data) throws Exception{

        Signature signature = Signature.getInstance("");


        return null;
    }
}
