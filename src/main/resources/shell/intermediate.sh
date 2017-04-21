#!/usr/bin/env bash


# create intermediate ca
mkdir /root/ca/intermediate
cd /root/ca/intermediate
mkdir certs crl csr newcerts private
chmod 700 private
touch index.txt
echo 1000 > serial
echo 1000 > crlnumber

# update the configuration file
cp -p /root/ca/openssl.cnf /root/ca/intermediate/openssl.cnf
vi /root/ca/intermediate/openssl.cnf

# create intermediate key, input 'intermediate' as secret key
openssl genrsa -aes256 \
      -out private/intermediate.key.pem 4096
chmod 400 private/intermediate.key.pem

# create intermediate certificate, after signed, the index.txt will have a record for the it
openssl req -config openssl.cnf -new -sha256 \
      -key private/intermediate.key.pem \
      -out csr/intermediate.csr.pem
cd /root/ca
openssl ca -config openssl.cnf -extensions v3_intermediate_ca \
      -days 3650 -notext -md sha256 \
      -in intermediate/csr/intermediate.csr.pem \
      -out intermediate/certs/intermediate.cert.pem
chmod 444 intermediate/certs/intermediate.cert.pem

# verify the intermediate cert information
openssl x509 -noout -text -in intermediate/certs/intermediate.cert.pem

# create the certificate chain file
cd /root/ca
cat intermediate/certs/intermediate.cert.pem \
      certs/ca.cert.pem > intermediate/certs/ca-chain.cert.pem
chmod 444 intermediate/certs/ca-chain.cert.pem