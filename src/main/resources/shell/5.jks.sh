#!/usr/bin/env bash

# create truststore, input 'certjks'
cd /root/ca
mkdir jks
keytool -import -trustcacerts -alias root -file certs/ca.cert.pem -keystore jks/ca.cert.jks

# create truststore, input 'certjks'
cd /root/ca/intermediate
mkdir jks
keytool -import -trustcacerts -alias root -file certs/intermediate.cert.pem -keystore jks/intermediate.cert.jks