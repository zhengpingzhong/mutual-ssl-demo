# mutual-ssl-demo

## 情景1 验证“非对称密钥对”的“加密”和“解密”

通过原始的“加密”的“密钥对文件”对信息进行“公钥加密，私钥解密”和“私钥加密，公钥解密”的验证

参考情景 EncryptAndDecrypt


## 情景2 验证“数字证书”是否由“根证书签发”

数字证书通过CA私钥进行解密，判断是否真证书

参考 VerifyCertificate


## 情景3 验证“数字证书”签名


## 情景4 服务端的单向 SSL 沟通

服务端安装数字证书，然后终端没有安装，进行单向 SSL 沟通

参考 SingleSSLCommunication


## 情景5 服务端 和 终端 的双向 SSL 沟通
 
 服务端安装数字证书，终端安装数字证书，进行双向 SSL 沟通
 
 参考 MutualSSLCommunication



