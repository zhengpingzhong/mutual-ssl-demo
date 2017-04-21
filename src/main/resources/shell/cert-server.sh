#!/usr/bin/env bash



# Although 4096 bits is slightly more secure than 2048 bits, it slows down TLS handshakes and significantly increases processor load during handshakes.
# For this reason, most websites use 2048-bit pairs.


# create a certificate, input 'example' as secret password
cd /root/ca
openssl genrsa -aes256 \
      -out intermediate/private/www.example.com.key.pem 2048
chmod 400 intermediate/private/www.example.com.key.pem
openssl req -config intermediate/openssl.cnf \
      -key intermediate/private/www.example.com.key.pem \
      -new -sha256 -out intermediate/csr/www.example.com.csr.pem

# sign the csr
# if the certificate is going to be used on a server, use the server_cert extension. if the certificate is going to be used for user authentication, use the usr_cert extension.
#
openssl ca -config intermediate/openssl.cnf \
      -extensions server_cert -days 375 -notext -md sha256 \
      -in intermediate/csr/www.example.com.csr.pem \
      -out intermediate/certs/www.example.com.cert.pem
chmod 444 intermediate/certs/www.example.com.cert.pem

# verify the certificate
openssl x509 -noout -text -in /root/ca/intermediate/certs/www.example.com.cert.pem