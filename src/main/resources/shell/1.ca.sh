#!/usr/bin/env bash

# create directory
mkdir /root/ca

# create related directory
cd /root/ca
mkdir certs crl newcerts private
chmod 700 private
touch index.txt
echo 1000 > serial

# put openssl.cnf into /root/intermediate/openssl.cnf

# create root key pair, input 'root' as secret password
# intermediate.key.pem contains the 'public key' and 'private key' together
openssl genrsa -aes256 -out private/ca.key.pem 4096
chmod 400 private/ca.key.pem

# get the 'public key' from the ca.key.pem file
# openssl rsa -in private/intermediate.key.pem -out private/intermediate.public.pem -outform PEM -pubout

# create root certificate
openssl req -config openssl.cnf \
      -key private/ca.key.pem \
      -new -x509 -days 7300 -sha256 -extensions v3_ca \
      -out certs/ca.cert.pem
chmod 444 certs/ca.cert.pem

# verify the cert information
openssl x509 -noout -text -in certs/ca.cert.pem