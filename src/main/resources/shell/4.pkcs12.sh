#!/usr/bin/env bash

# convert the cert + private key to p12, enter 'p12' as export password
cd /root/ca/intermediate
mkdir p12
openssl pkcs12 -export \
    -inkey private/www.example.com.key.pem \
    -in certs/www.example.com.cert.pem \
    -out p12/www.example.com.p12

