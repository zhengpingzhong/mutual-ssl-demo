# mutual-ssl-demo

## 情景1 验证非对称密钥对 

公钥加密，然后私钥解密。私钥加密，然后公钥解密。
参考 EncryptAndDecrypt.java


## 情景2 验证CA根证书数字证书

数字证书通过CA私钥进行解密，判断是否真证书
参考 VerifyCertificate.java


## 情景3 服务端的单向 SSL 沟通

服务端安装数字证书，然后终端没有安装，进行单向 SSL 沟通
参考 SingleSSLCommunication.java


## 情景4 服务端 和 终端 的双向 SSL 沟通
 
 服务端安装数字证书，终端安装数字证书，进行双向 SSL 沟通
 参考 MutualSSLCommunication.java



