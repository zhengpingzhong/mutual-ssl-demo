package com.sightcorner;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSLCertificateValidator {

    // 自定义TrustManager，用于验证证书
    static class CustomTrustManager implements X509TrustManager {
        private X509TrustManager defaultTrustManager;

        public CustomTrustManager(TrustManagerFactory tmf) throws NoSuchAlgorithmException, KeyStoreException {
            tmf.init((KeyStore) null);
            TrustManager[] trustManagers = tmf.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + trustManagers);
            }
            this.defaultTrustManager = (X509TrustManager) trustManagers[0];
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            defaultTrustManager.checkClientTrusted(chain, authType);
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // 你可以在这里添加额外的验证逻辑
            // 例如，检查特定的证书属性或颁发机构
            System.out.println("验证服务器证书链:");
            for (X509Certificate cert : chain) {
                System.out.println(cert);
            }
            defaultTrustManager.checkServerTrusted(chain, authType);
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return defaultTrustManager.getAcceptedIssuers();
        }
    }

    public static void main(String[] args) {
        try {
            // 创建一个SSLContext，并指定我们的自定义TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            sslContext.init(null, new TrustManager[]{new CustomTrustManager(tmf)}, new java.security.SecureRandom());

            // 创建一个SSLSocketFactory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // 打开一个HTTPS连接
            URL url = new URL("https://if.zqdialup.cn:20080/dialup-controller//sdk/shortProcessReport?sdkType=3&sdkVersion=1.3.0&token=b772bf015c7917d2ff5147eed3dccec5&data=%7B%22businessCode%22%3A%22YWPT-ZHQI-YWBC-084%22%2C%22clientId%22%3A%225a9ebb062d1d54f12b75c9184dea3574%22%2C%22eventCode%22%3A%22login%22%2C%22eventName%22%3A%22%E7%99%BB%E9%99%86%22%2C%22eventType%22%3A%221%22%2C%22eventValue%22%3A%222024-10-22%2011%3A14%3A31%3A917%22%2C%22eventStatus%22%3A%221%22%2C%22exceptionCode%22%3A%22%22%2C%22exceptionMsg%22%3A%22%22%2C%22apiTime%22%3A%222024-10-22%2011%3A14%3A31%22%2C%22sdkType%22%3A%223%22%2C%22factory%22%3A%22%22%2C%22model%22%3A%22%22%2C%22operatingSystem%22%3A%22Windows%22%2C%22operatingSystemVersion%22%3A%22Windows%2010%22%2C%22token%22%3A%22b772bf015c7917d2ff5147eed3dccec5%22%2C%22netFlag%22%3A%22%22%2C%22carrierName%22%3A%22%22%2C%22sdkVersion%22%3A%221.3.0%22%2C%22appName%22%3A%22%E9%9B%86%E5%AE%A2%E4%B8%9A%E5%8A%A1%E6%8B%A8%E6%B5%8B%E5%B9%B3%E5%8F%B0%22%2C%22appVersion%22%3A%22%22%2C%22passwordFlag%22%3A%221%22%2C%22eventSeq%22%3A1729566871918%2C%22browser%22%3A%22Chrome%22%2C%22browserVersion%22%3A129%7D");
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setSSLSocketFactory(sslSocketFactory);

            // 发起GET请求并读取响应
            BufferedReader in = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 打印响应
            System.out.println(response.toString());

        } catch (SSLException e) {
            System.err.println("SSL验证失败: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO错误: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
