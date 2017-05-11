package com.sightcorner.scenario;



import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.Certificate;

public class SingleSSLCommunicate {

    //服务端安装数字证书，然后终端没有安装，进行单向 SSL 沟通

    //1. 配置 tomcat 的 数字证书
    /*
        <Connector port="8443" protocol="org.apache.coyote.http11.Http11Protocol"
               SSLEnabled="true"
               maxThreads="150" scheme="https" secure="true"
               sslProtocol="TLS" clientAuth="false"
               keystoreType="PKCS12"
               keystoreFile="/p12/www.example.com.p12"
               keystorePass="p12"
               />
     */

    //如果keystorePass错误的话，通过浏览器访问，会建立连接失败（拒绝了我们的连接请求。）
    //如果是keystoreFile路径错误，通过浏览器访问，会建立连接失败（拒绝了我们的连接请求。）
    //keystoreFile 和 keystorePass 都是正确后，会被浏览器拦截（您的连接不是私密连接，此网站出具的安全证书是为其他网站地址颁发的。此网站出具的安全证书不是由受信任的证书颁发机构颁发的。）

    //测试情景：只添加 ca.cert，会被浏览器拦截
    //测试情景：只添加 intermediate.cert，会被浏览器拦截
    //测试情景：只添加 intermediate.cert + ca.cert，正常连接
    //这里要注意一下，由于颁发的数字证书是指定特定域名的。如果通过ip访问，还是会被浏览器拦截的。可以通过修改hosts文件，通过域名来指定ip的话，通过域名在浏览器访问就会显示连接正常了。
    //显示异常：https://192.168.xxx.xxx:8443/
    //显示正常：https://www.example.com:8443/


    public static void main(String[] args) throws Exception{
        new SingleSSLCommunicate().connect();
    }

    private void connect() throws Exception{
        String urlPath = "https://www.example.com:8443/";
        URL url = new URL(urlPath);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        printCertificate(httpsURLConnection);
    }

    private void printCertificate(HttpsURLConnection httpsURLConnection) throws Exception {
        System.out.println(httpsURLConnection.getResponseCode());
        System.out.println(httpsURLConnection.getCipherSuite());
        Certificate[] certificates = httpsURLConnection.getServerCertificates();
        for(Certificate certificate: certificates) {
            System.out.println(certificate.getType());
        }

        System.out.println();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
        String input;
        while( (input = bufferedReader.readLine()) != null) {
            System.out.println(input);
        }
        bufferedReader.close();
    }

}
