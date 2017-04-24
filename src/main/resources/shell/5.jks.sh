#!/usr/bin/env bash

# create truststore, input 'certjks'
cd /root/ca/intermediate
keytool -import -trustcacerts -alias root -file certs/intermediate.cert.pem -keystore jks/intermediate.cert.jks